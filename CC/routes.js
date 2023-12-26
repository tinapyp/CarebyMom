// routes.js
const Handlers = require('./handler');
const Joi = require('joi');

module.exports = [
  {
    method: 'POST',
    path: '/signup',
    handler: Handlers.signup,
    options: {
      auth: false,
      validate: {
        payload: Joi.object({
          username: Joi.string().allow(''),
          email: Joi.string().email().allow(''),
          password: Joi.string().allow(''),
          periodTime: Joi.string().required(),
        }),
        failAction: (request, h, error) => {
          throw error;
        }
      }
    }
  },
  {
    method: 'POST',
    path: '/login',
    handler: Handlers.login,
    options: {
      auth: false,
      validate: {
        payload: Joi.object({
          username: Joi.string().allow('').required(),
          password: Joi.string().allow('').required()  
        })
      }
    }
  },
  // Add a new route for logout
  {
    method: 'POST',
    path: '/logout',
    handler: Handlers.logout,
    options: {
      auth: 'jwt', // Require authentication using JWT
    },
  },

  // Add a new route for changing password
{
  method: 'POST',
  path: '/change-password',
  handler: Handlers.changePassword,
  options: {
  validate: {
      payload: Joi.object({
      currentPassword: Joi.string().required(),
      newPassword: Joi.string().required(),
      }),
      failAction: (request, h, error) => {
      throw error;
      },
  },
  auth: 'jwt',
  },
},
];
