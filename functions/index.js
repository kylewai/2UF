const functions = require('firebase-functions');
const  requests = require('request');
const express = require('express');
const app = express();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, res) => {
    const url = "https://one.ufl.edu/apix/soc/schedule/?category=RES&term=2201";
    requests.get(url, (error, response, body) => {
        console.log(body);
        //let json = JSON.parse(body);
        console.log('error:', error);
        //res.json(json);
        if(error){
            res.send("Oh no");
        }
    });
});

// const express = require('express');

// const app = express();

// app.get('/soc', (req, res) => {
//     res.send('Hello from firebase cloud functions');
// });

// module.exports = functions.https.onRequest(app);