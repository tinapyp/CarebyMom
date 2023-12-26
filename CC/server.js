// server.js
const Hapi = require('@hapi/hapi');
const routes = require('./routes');
const HapiJwt = require('hapi-auth-jwt2');
const mysql = require('mysql2');
const Users = require('./users');

const init = async () => {
  const server = Hapi.server({
    port: process.env.PORT || 4000,
    host: '0.0.0.0',
  });

  // Register the 'hapi-auth-jwt2' plugin
  await server.register(HapiJwt);

  // Configure the JWT authentication strategy
  server.auth.strategy('jwt', 'jwt', {
    key: 'n8ewhd2d',
    validate: (decoded, request, h) => {
      // Implement your own token validation logic here
      return { isValid: true };
    },
    verifyOptions: { algorithms: ['HS256'] },
  });

  server.auth.default('jwt');
  
  server.route(routes);

  await server.start();
  console.log('Server running on %s', server.info.uri);
};

process.on('unhandledRejection', (err) => {
  console.log(err);
  process.exit(1);
});

init();