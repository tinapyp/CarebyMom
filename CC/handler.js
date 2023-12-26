// handler.js
const Users = require('./users');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const generateToken = (user) => {
  return jwt.sign({ username: user.username, email: user.email }, 'secret-key', { expiresIn: '1h' });
};

module.exports = {
    signup: async (request, h) => {
        try {
          const { username, email, password, periodTime } = request.payload;
    
          // Custom validation
          if (!username) {
            return h.response({ message: 'Username is required' }).code(400);
          }
    
          if (!email) {
            return h.response({ message: 'Email is required' }).code(400);
          }
    
          if (!password) {
            return h.response({ message: 'Password is required' }).code(400);
          }
    
          if (!periodTime) {
            return h.response({ message: 'periodTime is required' }).code(400);
          }
    
          const existingUsername = await Users.getUserByUsername(username);
          if (existingUsername) {
            return h.response({ message: 'Username already exists' }).code(400);
          }
    
          const existingEmail = await Users.getUserByEmail(email);
          if (existingEmail) {
            return h.response({ message: 'Email already exists' }).code(400);
          }
    
          const hashedPassword = bcrypt.hashSync(password, 10);
          const newUser = await Users.addUser({ username, email, password: hashedPassword, periodTime });
          const token = generateToken(newUser);
    
          // Adjust the response structure to include the token in the user object
          return h.response({
            message: 'Signup successful',
            user: {
              id: newUser.id,
              username: newUser.username,
              email: newUser.email,
              password: newUser.password, // Note: Including the password might not be ideal in a real-world scenario
              periodTime: newUser.periodTime,
              token: token,
            },
          }).code(201);
        } catch (error) {
          console.error(error);
          // Return a custom error response without Hapi.js validation details
          return h.response({ message: 'Error processing the request' }).code(500);
        }
      },         

      login: async (request, h) => {
        try {
            const { username } = request.payload;
    
            // Validasi apakah username diisi
            if (!username) {
                return h.response({ message: 'Invalid username' }).code(401);
            }
    
            const user = await Users.getUserByUsername(username);
    
            if (!user) {
                // Jika username tidak ditemukan
                return h.response({ message: 'Invalid username' }).code(401);
            }
    
            // Jika kedua kondisi tidak terpenuhi, login berhasil
            const token = generateToken(user);
    
            // Adjust the response structure to include the token in the user object
            return h.response({
                message: 'Login successful',
                user: {
                    id: user.id,
                    username: user.username,
                    email: user.email,
                    periodTime: user.periodTime,
                    token: token,
                },
            }).code(200);
        } catch (error) {
            console.error(error);
            return h.response({ message: 'Error processing the request' }).code(500);
        }
    },    
      
  logout: async (request, h) => {
    try {
      const credentials = request.auth.credentials;
      if (!credentials) {
        return h.response({ message: 'Unauthorized' }).code(401);
      }

      return h.response({ message: 'Logout successful' }).code(200);
    } catch (error) {
      console.error(error);
      return h.response({ message: 'Error processing the request' }).code(500);
    }
  },

  changePassword: async (request, h) => {
    try {
      const { currentPassword, newPassword } = request.payload;
  
      const credentials = request.auth.credentials;
      if (!credentials) {
        return h.response({ message: 'Unauthorized' }).code(401);
      }
  
      const user = await Users.getUserByUsername(credentials.username);
  
      // Check if the provided currentPassword matches the latest hashed password
      const isPasswordValid = bcrypt.compareSync(currentPassword, user.password);
      if (!isPasswordValid) {
        return h.response({ message: 'Invalid current password' }).code(401);
      }
  
      // Generate a new hashed password for the newPassword
      const hashedNewPassword = bcrypt.hashSync(newPassword, 10);
  
      // Update the user's password in the database using the user's id
      await Users.updateUserPassword(user.id, hashedNewPassword);
  
      return h.response({ message: 'Password changed successfully' }).code(200);
    } catch (error) {
      console.error(error);
      return h.response({ message: 'Error processing the request' }).code(500);
    }
  },  
};
