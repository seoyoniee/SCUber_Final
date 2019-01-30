// Import package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs');
var multer = require('multer');
var path = require('path');


//PASSWORD UTILS
//CREATE FUNCTION TO RANDOM SALT
var genRandomString = function(length) {
  return crypto.randomBytes(Math.ceil(length/2)).toString('hex').slice(0,length);
};

var sha512 = function(password,salt) {
  var hash = crypto.createHmac('sha512',salt);
  hash.update(password);
  var value = hash.digest('hex');
  return {
    salt:salt,
    passwordHash:value
  };
};

function saltHashPassword(userPassword) {
  var salt = genRandomString(16); // Create 16 random character
  var passwordData = sha512(userPassword,salt);
  return passwordData;
}

function checkHashPassword(userPassword,salt) {
  var passwordData = sha512(userPassword,salt);
  return passwordData;
}

//Create Express Service
var app = express();
app.use(bodyParser.json({limit : '50mb'}))
app.use(bodyParser.urlencoded({ extended: true, limit : '50mb' }));

//Create MongoDB Client
var MongoClient = mongodb.MongoClient;

//Connection urlencoded
var url = 'mongodb://localhost:27017' // 27017 is default port

MongoClient.connect(url,{useNewUrlParser: true},function(err,client) {
  if (err)
    console.log('Unable to connect to the mongoDB server.Error', err);
  else {
    //Register
    app.post('/register',(request,response,next)=>{
      var post_data = request.body;

      var plaint_password = post_data.password;
      var hash_data = saltHashPassword(plaint_password);

      var password = hash_data.passwordHash; // Save password hash
      var salt = hash_data.salt; // Save saltHAshPassword

      var name = post_data.name;
      var id = post_data.id;
      var phonenum = post_data.phonenum;

      //해당 프로필 사진을 인코딩해주는거야
      var imageAsBase64 = fs.readFileSync('./icon.png', 'base64');

      var insertJson = {
        'id': id,
        'password': password,
        'profile' : imageAsBase64,
        'salt':salt,
        'phonenum':phonenum,
        'name':name,
        'point' : 1000,
        'noShow' : 0,
        'late' : 0
      };
      var db = client.db('scuber');

      //Check exists id
      db.collection('user').find({'id':id}).count(function(err,number){
        if (number != 0) {
          response.json('ID already exists');
          //console.log('ID already exists');
        }
        else {
          //Insert hash_data
          db.collection('user').insertOne(insertJson,function(error,res) {
            response.json('Registration success');
            //console.log('Registration success');
          })
        }
      })
    });

//login 하는 부분
    app.post('/login',(request,response,next)=>{
      var post_data = request.body;

      var id = post_data.id;
      var userPassword = post_data.password;


      var db = client.db('scuber');

      //Check exists id
      db.collection('user').find({'id':id}).count(function(err,number){
        if (number == 0) {
          response.json('ID not exists');
          //console.log('ID not exists');
        }
        else {
          //Insert hash_data
          db.collection('user').findOne({'id':id},function(err,user){
            var salt = user.salt;
            var encrypted_password = checkHashPassword(userPassword,salt).passwordHash; // HAsh password with saltHAshPassword
            var hashed_password = user.password; // Get password from user
            if (hashed_password == encrypted_password) {
              response.send(id);
              console.log('Login success');
            }
            else {
              response.json('Wrong password');
            //  console.log('Wrong password');
            }
          })
        }
      })
    });


//모든 유저 정보 가져와
    app.get('/users',(request,response,next)=>{
      var db = client.db('scuber');

      db.collection('user').find({}).toArray(function(err, result) {
        if(err) throw err;
        //console.log('get all users');
        response.json(result);
      });
      });


//해당 id에 대한 정보를 가져와
app.post('/userInfo',(request,response,next)=>{
  var id = request.body.id;

  var db = client.db('scuber');

      db.collection('user').findOne({'id':id}, function(err,userInfo){
      if(err) throw err;
      console.log(userInfo);
      response.json(userInfo);
    });
    });




// 해당 id를 찾아서 유저의 정보를 삭제해줘
      app.delete('/users/:user_id', (request,response,next)=>{
        var db = client.db('scuber');

        db.collection('user').deleteOne({id: request.params.user_id}, function(err, obj) {
          if(err) throw err;
          //console.log('delete one user');
          response.json('delete one user');
        });

      });

//해당 id를 찾아서 유저의 프로필 사진을 수정해줘
    app.post('/updateProfile', (request, response, next) => {
      var post_data = request.body;
      var id = post_data.id;
      var newProfile = post_data.profile;
      //var newName = post_data.name

      var db = client.db('scuber');
      console.log('hello');
      console.log(id, newProfile);
      db.collection('user').updateOne({'id':id}, {$set:{'profile':newProfile}}, function(err, result) {
        if(err) throw err;
        console.log('update user info');
        response.json('update user info');
      });
    });


//call을 요청하면 등록해줘
      app.post('/registerCall',(request,response,next)=>{
        var post_data = request.body;

        var id = post_data.id;
        var from = post_data.from;
        var to = post_data.to;
        var time_hour = post_data.time_hour;
        var time_min = post_data.time_min;
    //    var bitmap = post_data.bitmap;
        var defaultState = "match waiting";

        var insertJson = {
          'id': id,
          'from': from,
          'to': to,
          'time_hour': time_hour,
          'time_min': time_min,
          'giver' : id, //나중에 요청 accpet해주면 id 받아와서 바꿔야대
        //  'bitmap' : bitmap,
          'state' : defaultState
        };

        var db = client.db('scuber');

          //data를 넣어주자!
            db.collection('call').insertOne(insertJson,function(error,res) {
              response.send(ObjectID());
              console.log(ObjectID());
            });

        });


        //모든 call 정보 가져와
            app.get('/calls',(request,response,next)=>{
              var db = client.db('scuber');

              db.collection('call').find({}).toArray(function(err, result) {
                if(err) throw err;
                //console.log('get all calls');
                response.json(result);
              });
              });


        //해당 taker의 모든 call들을 보여줘
        app.post('/takerCalls',(request,response,next)=>{

          var post_data = request.body;
          var id = post_data.id;

          var db = client.db('scuber');

          db.collection('call').find({'id':id}).toArray(function(err, result) {
            if(err) throw err;
          //  console.log(result);
            response.json(result);
          });
          });



          //해당 giver의 모든 call들을 보여줘
          app.post('/giverCalls',(request,response,next)=>{
            var post_data = request.body;
            var giver = post_data.giver;

            var db = client.db('scuber');

            console.log(giver);
            db.collection('call').find({'giver': giver}).toArray(function(err, result) {
              if(err) throw err;
              console.log(result);
              response.json(result);
            });
            });



            // 해당 call의 _id를 요청한 유저의 id를 반환
                  app.post('/returnID', (request,response,next)=>{

                    var post_data = request.body;
                    var _id = post_data._id;

                    var db = client.db('scuber');

                    console.log(_id);
                    db.collection('call').findOne({"_id":ObjectID(_id)}, function(err, callInfo) {
                      if(err) throw err;
                      console.log(callInfo);
                      console.log(callInfo.id);
                      response.send(callInfo.id);
                    });

                  });



                        // 해당 call의 giver의
                              app.post('/returnGiverID', (request,response,next)=>{

                                var post_data = request.body;
                                var _id = post_data._id;

                                var db = client.db('scuber');

                                console.log(_id);
                                db.collection('call').findOne({"_id":ObjectID(_id)}, function(err, callInfo) {
                                  if(err) throw err;
                                  console.log(callInfo);
                                  console.log(callInfo.giver);
                                  response.send(callInfo.giver);
                                });

                              });



        // 해당 id의 call를 삭제
              app.delete('/calls/:call_id', (request,response,next)=>{
                var db = client.db('scuber');

                db.collection('call').deleteOne({"_id":ObjectID(request.params.call_id)}, function(err, obj) {
                  if(err) throw err;
                  //console.log('delete one call');
                  //console.log(request.params.call_id);
                  response.json('delete one call');
                });

              });



        // 모든 call들를 삭제
                    app.delete('/Deletecalls', (request,response,next)=>{
                      var db = client.db('scuber');

                      db.collection('call').deleteMany({}, function(err, obj) {
                        if(err) throw err;
                        //console.log('delete one call');
                        //console.log(request.params.call_id);
                        response.json('delete all calls');
                      });

                    });



    //해당 call의 state를 바꿔줘야대
      app.post('/updateCallState', (request, response, next) => {
        var post_data = request.body;

        var _id = post_data._id;

        var newState = post_data.state;
        var giver = post_data.giver;

        //console.log(_id, newState, giver);

        var db = client.db('scuber');
        db.collection('call').updateOne({"_id":ObjectID(_id)}, {$set:{'state':newState, 'giver' : giver}}, function(err, result) {
          if(err) throw err;
          //console.log(result);
            console.log('update call state');
            response.json('update call state');
          });
          });

          //해당 call의 state를 바꿔줘야대
            app.post('/updateCallState2', (request, response, next) => {
              var post_data = request.body;

              var _id = post_data._id;

              var newState = post_data.state;


              //console.log(_id, newState, giver);

              var db = client.db('scuber');
              db.collection('call').updateOne({"_id":ObjectID(_id)}, {$set:{'state':newState}}, function(err, result) {
                if(err) throw err;
                //console.log(result);
                  console.log('update call state2');
                  response.json('update call state2');
                });
                });



          //해당 유저의 포인트를 +해줘
            app.post('/pointChangePlus', (request, response, next) => {
              var post_data = request.body;

              var id = post_data.id;
              var changePoint = post_data.point;

              //console.log(id,chargePoint);

              var db = client.db('scuber');
              db.collection('user').findOne({"id":id}, function(err,userInfo){
              var point = userInfo.point;

              db.collection('user').updateOne({"id":id}, {$set:{'point': Number(changePoint) + Number(point)}}, function(err, result) {
              if(err) throw err;
                console.log('charge completed');
                response.json('charge completed');
               });
              });
            });

            //해당 유저의 noShow 값을 +해줘
              app.post('/noShowPlus', (request, response, next) => {
                var post_data = request.body;

                var id = post_data.id;


                var db = client.db('scuber');
                db.collection('user').findOne({"id":id}, function(err,userInfo){
                var noShow = userInfo.noShow;

                db.collection('user').updateOne({"id":id}, {$set:{'noShow': Number(noShow) + 1}}, function(err, result) {
                if(err) throw err;
                  console.log(result);
                  response.json('noShow +1');
                 });
                });
              });

              //해당 유저의 late 값을 +해줘
                app.post('/latePlus', (request, response, next) => {
                  var post_data = request.body;

                  var id = post_data.id;


                  var db = client.db('scuber');
                  db.collection('user').findOne({"id":id}, function(err,userInfo){
                  var late = userInfo.late;

                  db.collection('user').updateOne({"id":id}, {$set:{'late': Number(late) + 1}}, function(err, result) {
                  if(err) throw err;
                    console.log('late +1');
                    response.json('late +1');
                   });
                  });
                });


            //해당 유저의 포인트를 -해줘
              app.post('/pointChangeMinus', (request, response, next) => {
                var post_data = request.body;

                var id = post_data.id;
                var changePoint = post_data.point;

                //console.log(id,chargePoint);

                var db = client.db('scuber');
                db.collection('user').findOne({"id":id}, function(err,userInfo){
                var point = userInfo.point;

                db.collection('user').updateOne({"id":id}, {$set:{'point':  Number(point) - Number(changePoint)}}, function(err, result) {
                if(err) throw err;
                  console.log('charge completed');
                  response.json('charge completed');
                 });
                });
              });



    //Start Web Server
    app.listen(80,()=>{
      console.log('Connected to MongoDB Server, WebService running on port 80');
    })
  }
})
