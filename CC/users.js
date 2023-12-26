const mysql = require('mysql2');
const bcrypt = require('bcrypt');

let connection;

if (process.env.NODE_ENV === 'NODE_ENV') {
  connection = mysql.createConnection({
    host: 'host',
    user: 'user',
    password: 'password',
    database: 'database',
  });
}

const initializeDatabase = () => {
  return new Promise((resolve, reject) => {
    const query = `
      CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL
      );
    `;
    connection.query(query, (error) => {
      if (error) {
        console.error('Error creating user table:', error);
        reject(error);
      } else {
        console.log('User table created or already exists.');
        resolve();
      }
    });
  });
};

const getUserByUsername = (username) => {
  const query = `SELECT * FROM users WHERE username = ?`;
  return new Promise((resolve, reject) => {
    connection.query(query, [username], (error, results) => {
      if (error) {
        reject(error);
      } else {
        resolve(results[0]);
      }
    });
  });
};

const getUserByEmail = (email) => {
  const query = `SELECT * FROM users WHERE email = ?`;
  return new Promise((resolve, reject) => {
    connection.query(query, [email], (error, results) => {
      if (error) {
        reject(error);
      } else {
        resolve(results[0]);
      }
    });
  });
};

const addUser = async (user) => {
  const { username, email, password, periodTime } = user;
  const hashedPassword = await bcrypt.hash(password, 10);

  const insertQuery = 'INSERT INTO users (username, email, password, periodTime) VALUES (?, ?, ?, ?)';
  return new Promise((resolve, reject) => {
    connection.query(insertQuery, [username, email, hashedPassword, periodTime], (error, results) => {
      if (error) {
        reject(error);
      } else {
        resolve({ ...user, id: results.insertId });
      }
    });
  });
};

const updateUserPassword = (userId, hashedNewPassword) => {
  const updateQuery = 'UPDATE users SET password = ? WHERE id = ?';
  return new Promise((resolve, reject) => {
    connection.query(updateQuery, [hashedNewPassword, userId], (error, results) => {
      if (error) {
        reject(error);
      } else {
        resolve({ message: 'Password changed successfully' });
      }
    });
  });
};

module.exports = {
  initializeDatabase,
  getUserByUsername,
  getUserByEmail,
  addUser,
  updateUserPassword,
};
