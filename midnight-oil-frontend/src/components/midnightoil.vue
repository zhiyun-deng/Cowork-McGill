<template>

  <div id="midnightoil" class="midnightoil">
  <nav class="navbar navbar-light px-5" style="background-color: #FFFFFF;">
  <!-- Navbar content -->
  <a class="navbar-brand text-wrap mx-md-auto ml-lg-0" href="#/app"><h2>Co-work McGill Random Pairing <i class="bi bi-people-fill"></i></h2></a>
  
  <div class="col-sm-12 col-md-5 text-sm-center text-xl-right">
  <button type="button" class="btn btn-outline-danger ml-auto mr-xl-4" @click="showSessions()">
  Remembered sessions
  </button>
  <button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#exampleModal" @click="superProcessCookie()">
  How to use
  </button>
  </div>
  
</nav>

<div class="modal" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      
      <div class="modal-body">
        <b>Step 1: </b>Login to Zoom using SSO (mcgill.zoom.us). Other accounts are not supported. <br>
        <strong>Step 2: </strong>Book a session. Avoid booking multiple sessions if you cannot go to all of them.<br>
        <strong>Step 3: </strong>The browser now remembers your requests by default. If you would like to access the link from some other browser, record the request ID somewhere. This is your unique identifier for the booking. <br>
        <strong>Step 4: </strong>Check back later in the day. Click on Remembered Sessions to see status of requests remembered by browser. If a pairing has been made, you will find a Zoom link. Random pairing occurs every two hours. Alternatively, search your request ID in the search box.  <br>
        <strong>Step 5: </strong>Go to your virtual session. Start the session by telling each other what you will work on today.<br>
        <strong>Step 6: </strong>Get to work!<br>
      </div>
      
    </div>
  </div>
</div>

<div class="modal" id="pastRequestModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      
      <div class="modal-body">
          <table class="table" v-if="cookies">
            <thead>
              <tr>
                <th scope="col">Time</th>
                <th scope="col">Status</th>
                <th scope="col">--</th>
              </tr>
            </thead>
            <tr v-for="request in storedRequests">
              <td>{{request.timeslotString}}</td>
              <td>{{request.msg}}</td>
              <td>
                <button v-on:click = "deleteRequest(request)" type="button" class="btn btn-danger">Delete</button>
              </td>
            </tr>
        </table>
      </div>
      
    </div>
  </div>
</div>
    <!--<h2 style="text-align:center">Co-work McGill Random Pairing</h2>-->
    <p>
      <span style="color:red" v-if="error">{{errorMessage}}</span>
      <span>{{msg}}</span>
    </p>
    <div><img class="img-fluid" src='../assets/undraw_co-working_825n.svg'/></div>
    
    <br>
  <div>
  <!--
    <a href="https://zoom.us/oauth/authorize?response_type=code&client_id=CpVB04MsSzyrqxe6kYPzNw&redirect_uri=https://cowork-mcgill.herokuapp.com/%23/app">Login to Zoom</a>
  -->
    <a href="https://zoom.us/oauth/authorize?response_type=code&client_id=_NEnLdY1ReK8ApJ_IcGLw&redirect_uri=https%3A%2F%2Fcowork-mcgill.herokuapp.com%2F%23%2Fapp" class="btn btn-lg btn-primary"><i class="bi-shield-lock-fill"></i>Login to Zoom</a>
  </div>
  
  <br>
  <div>
    Search Request Status by Request ID: <input type="text" v-model="inputReqID" style="display: inline;">
     <button v-bind:disabled="!inputReqID" @click="searchReq(inputReqID)">Get Zoom Link</button>
  <br><br>
  </div>
  <h2>Book a work session by submitting a request</h2><br>
    <div class="container col-md-10 col-xs-12 text-center" style="margin: mx-0; width: 100%; height: 100%;  flex-direction: row; justify-content: center;">
     
     <table class="table" id="rememberedSessions">
      <thead>
        <tr>
          <th scope="col">Session Date</th>
          <th scope="col">Session Time</th>
          <th scope="col">Number of existing requests</th>
          <th scope="col">--</th>
        </tr>
      </thead>
      <tbody>
      <tr v-for="timeslot in times" :key="timeslot.startDate">
        <td>{{timeslot.startDate}}</td>
        <td>{{timeslot.startTime}}</td>
        <td>{{timeslot.numRequests}}</td>
        <td>
          <button v-on:click = "createRequest(timeslot)" type="button" class="btn btn-primary">Book</button>
        </td>
      </tr>
      </tbody>
     </table>
    </div>
  </div>
  
</template>

<script>
import axios from 'axios'

var config = require('../../config')

var frontendUrl = 'https://' + config.build.host + ':' + config.build.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
function RequestDto (msg, timeslotString, id) {
    this.msg=msg;
    this.timeslotString = timeslotString;
    this.id = id;
  }
export default {
  name: 'midnightoil',
  data () {
    return {
      msg: '',
      code: "",
      accessToken: "",
      error: false,
      times: [],
      inputReqID: "",
      errorMessage: "",
      link: "",
      storedRequests: [],
      cookies: [],
      updatedCookies: [],
      tempNewCookieString: ""
    };
  },
  created(){
    
    this.vm = this;
    this.code = this.$route.query.code;
    AXIOS.get('/times')
    .then(response=>{
      
      $('#rememberedSessions').DataTable().destroy();
      this.times=response.data;
      this.$nextTick(() => {
      $('#rememberedSessions').DataTable({
          
          "paging": true,
          "searching": true,
    "ordering":  false
        });
      });
    

    })
    .catch(e=>{
      this.error=true
      console.log(e)
    })
      
    if(this.code){
      AXIOS.get('/token?code=' + this.code)
      .then((response) => {
        console.log("successful login")
        this.error = false
        this.accessToken = response.data
        if(this.accessToken){
          document.cookie = 'Token=' + response.data
          alert("You are now logged into Zoom. ")
        }
        console.log(this.accessToken)
        

      })
      .catch(error => {
        this.error = true
        console.log(error)
      })
    }

  },
  mounted: function() {
        console.log(this.code);
        $(document).ready(function () {
        $('#rememberedSessions').DataTable();
});
    },
  methods:{
    showSessions: function(){
    this.processCookie();
    $('#pastRequestModal').modal('show');
    },
    searchReqCookie: function(reqID){
      var msg = '';
      var link = '';
      var timeslotString='';
      var res='';
      if (reqID === '') {
        return '';
      }
      AXIOS.get('/getrequest?id='+reqID)
    .then(response=>{
      console.log(response.data);
      link = response.data.zoomLink;
      timeslotString = response.data.timeslotString;
      if(link){
        msg = "Your link is "+link;
        console.log(msg);
      }
      else{
        msg = "Not paired";
        console.log(msg);
      }
      var request = new RequestDto(msg,timeslotString, reqID);
      this.storedRequests.push(request);
      this.updatedCookies.push(reqID);
      console.log(this.updatedCookies);
      res='x';
    })
    .catch(e=>{
      console.log(e)
      console.log('Cannot get request info')
      res='';
    })
    return res;
    },
    searchReq: function(reqID){
     if (reqID === '') {
        this.error = true
        this.msg = 'Request ID cannot be blank'
        return
      }
      console.log(reqID)
      AXIOS.get('/getrequest?id='+reqID)
    .then(response=>{
      console.log(response.data)
      this.link = response.data.zoomLink
      if(this.link){
        this.msg = "Your link is "+this.link
      }
      else{
        if(response.data.timeslotString){
            this.msg = "Your request has not yet been paired. The session is on " + response.data.timeslotString
        }
        else{
          this.msg = "This ID does not correspond to an existing request."
        }
        
      }
      $('html,body').scrollTop(0);
      
    })
    .catch(e=>{
      this.error=true
      this.errorMessage = 'Cannot get request info'
      console.log(e)
    })
    },
    getCookie: function(name){
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    },
    processCookie: async function(){
      // read cookies
      var temp = this.getCookie('hello')
      console.log(temp);
      this.updatedCookies.splice(0);
      this.storedRequests.splice(0);
      console.log(this.updatedCookies)
      if(!temp){
        return;
      }
      this.cookies = temp.split(' ');
      
      var newCookieString = "";
      const promises = [];

      // get request status
      console.log(this.cookies);
      for (let i=0;i<this.cookies.length;i++){
        var msg = '';
      var link = '';
      var timeslotString='';
      var res='';
      
      var vm = this;
      //console.log(reqID)
      AXIOS.get('/getrequest?id='+vm.cookies[i])
    .then(response=>{
      var reqID = vm.cookies[i];
      
      console.log('****'+reqID)
      console.log(response.data);
      if (reqID === '') {
        return '';
      }
      if(!response.data){
        return 'wonkie';
      }
      link = response.data.zoomLink;
      timeslotString = response.data.timeslotString;
      if(link){
        msg = "Your link is "+link;
        console.log(msg);
      }
      else{
        msg = "Not paired";
        console.log(msg);
      }
      var request = new RequestDto(msg,timeslotString,reqID);
      this.storedRequests.push(request);
      //updated cookies are nto empty for
      console.log('before:'+this.updatedCookies);
      this.updatedCookies.push(reqID);
      console.log(this.updatedCookies);
      res='x';
      newCookieString += reqID;
      newCookieString += ' ';
    })
    .catch(e=>{
      console.log(e)
      console.log('Cannot get request info')
      res='';
    })
    
    
        
        console.log("this is reached")
        console.log(this.updatedCookies);
        //if cookie is valid, add to string
        
      }
      return newCookieString;
      
      
    },
    superProcessCookie: function(){
      
      this.processCookie().then(newCookieString=>{
        //this.updatedCookies = ['hel', 'jkj']
      
      console.log(newCookieString);
      // update cookies to delete invalid cookies
      if(newCookieString){
        document.cookie = 'hello='+newCookieString.trim();
        console.log('aaaaaaaahhhhhhhhhhh')
      }
      
      else{
        document.cookie = 'hello=';
        console.log('cookie changed')
      }
      })
    },
    deleteRequest: function(req){
      var vm = this;
      if (this.accessToken === '') {
        if(this.getCookie('Token')){
          this.accessToken = this.getCookie('Token')
        }
        else{
        this.error = true
        this.msg = 'You must login to Zoom first.'
        alert(this.msg)
        return
        }

      }
      
     AXIOS.delete('/delete?id='+req.id+'&token='+this.accessToken)
    .then(response=>{
      this.msg = response.data
      alert(this.msg)
      if (this.msg.split(" ")[0]==="Successfully"){
        
        console.log('successfully deleted if statement')
        vm.superProcessCookie();
      }
      console.log(response.data)
      console.log('req.id '+req.id)
    })
    .catch(e=>{
      this.error=true
      console.log(e)
    })

    },
    createRequest: function(timeslot){
      var vm = this;
      if (this.accessToken === '') {
        if(this.getCookie('Token')){
          this.accessToken = this.getCookie('Token')
        }
        else{
        this.error = true
        this.msg = 'You must login to Zoom first.'
        alert(this.msg)
        return
        }

      }
      console.log(timeslot.startTime)
     AXIOS.post('/request?date='+timeslot.startDate+'&startTime='+timeslot.startTime+'&token='+this.accessToken)
    .then(response=>{
      this.msg = response.data
      alert(this.msg)
      if (this.msg.split(" ")[0]==="Success!"){
        var existing = vm.getCookie('hello');
        if (existing){
          document.cookie = 'hello='+existing+' '+this.msg.split(" ").pop();
        }
        else{
          document.cookie = 'hello='+this.msg.split(" ").pop();
        }
      }
      console.log(response.data)
    })
    .catch(e=>{
      this.error=true
      console.log(e)
    })

    }
  } 
}






</script>
<style>

div{
  text-align: center;
  font-family: 'Quicksand', sans-serif;
}
h1,h2,h3{
  font-family: 'Roboto', sans-serif;
}
.navbar
        {
            border-bottom:1px solid #000;
            box-shadow: 0 8px 16px 0 rgba(0,0,0,.2);
        }

</style>