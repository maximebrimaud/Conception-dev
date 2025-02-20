<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*,java.util.*,javax.servlet.*" %>
<%
	if (session.getAttribute("sessionId") == null || session.getAttribute("sessionId").equals("")){
		response.sendRedirect("LoginPage.jsp");
	}
%>

<html lang="en">
  <head>
    <meta>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <link rel="icon" href="Bootstrap/docs/favicon.ico">
    <title>WGTR - Movies Page</title>
    <!-- Bootstrap core CSS -->
    <link href="Bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">    
  </head>
  
  <body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="${pageContext.request.contextPath}/Home">Who Gets The Remote</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-left">
            <li><a href="${pageContext.request.contextPath}/Movies">Movies</a></li>
            <li><a href="${pageContext.request.contextPath}/Friends">Friends</a></li>
            <li><a href="${pageContext.request.contextPath}/Events">Events</a></li>         
          </ul>
		   <ul class="nav navbar-nav navbar-right">
            <li><a href="${pageContext.request.contextPath}/Profile">${userLogged.getFullName()}</a></li>
            <li><a href="LogoutPage.jsp">LogOut</a></li>
          </ul>
        </div>
      </div>
    </nav>
	<form role="form" action="Movies" method="post">
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-2 col-md-2 sidebar">

          </br>
          <h2 class="page-header">
          	
	          <div class="row placeholders">			  	
			  	<div class="col-xs-6 col-sm-12 placeholder">
	              <div align="center">
	              	<input id="SearchInput" class="form-control" name="SearchInput" placeholder="Search People">
	             	<input style="margin-top: 3px;" type="submit" class="btn btn-primary" id="btnSearch" name="btnSearch" value="Search Movies">	             	
	              </div>	             
	            </div>
	          </div>
         
          </h2>
           <ul class="nav nav-sidebar">	
          	<li id="btnCreateMovie" style="display: none;"><a href="${pageContext.request.contextPath}/CreateFilm">Create Movie</a></li> <!-- MoviesCreate.jsp    ////   CreateFilmData -->
          	<li id="btnManageCategories" style=" display: none;"><a href="${pageContext.request.contextPath}/MovieCategories">Manage Categories</a></li>             
            <li ><a  style="text-decoration: underline;" href="${pageContext.request.contextPath}/Movies" >Overview<!-- <span class="sr-only">(current)</span> --></a></li>                        
          </ul>
        </div>
        
        <div class="col-sm-10 col-md-10 main">
          <br/>
          <h1 class="page-header">Search Movies Result</h1>
         	
          <div class="row placeholders">		  
		  	<c:forEach items="${SearchMovieList}" var="item">
		  		<div class='col-xs-6 col-sm-3 placeholder'>
			        <div align='center'>
			        	<img src='${item.getImageFilm()}' width='190' height='190' class='img-responsive' alt='Generic placeholder thumbnail'>
					</div>
			        <h4 align='center'><a href="${pageContext.request.contextPath}/UpdateFilm?filmId=${item.getId()}" >${item.getName()}</a></h4>
			        <div align='center'>
			        	<span class='text-muted'>IMDB Rating: ${item.getNotationFilm()}/10</span>
			        </div>
		        </div>
		  	</c:forEach>
          </div>  
        </div>
        <div class="col-sm-2 col-md-2 sidebar">
          </br>
        
        </div>
        <div class="col-sm-10 col-md-10 main">
          <br/>
          <h2 class="page-header">Movies</h2>
          <div class="row placeholders">
		  	<c:forEach items="${listFilm}" var="item">
		  		<div class='col-xs-6 col-sm-3 placeholder'>
			        <div align='center'>
			        	<img src='${item.getImageFilm()}' width='190' height='190' class='img-responsive' alt='Generic placeholder thumbnail'>
					</div>
			        <h4 align='center'><a href="${pageContext.request.contextPath}/UpdateFilm?filmId=${item.getId()}" >${item.getName()}</a></h4>
			        <div align='center'>

			        	<span class='text-muted'>IMDB Rating: ${item.getNotationFilm()}/10</span>

			        </div>
		        </div>
		  	</c:forEach>
          </div>  
        </div>
      </div>
    </div>
 	</form>
	
    <br/><br/><br/>
    <div style="background-color:black; color:white; clear:both; text-align:center; padding:5px; padding-top:5px"> 
    	Who Gets The Remote - The Place for movie lovers
    </div>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="Bootstrap/docs/assets/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="Bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="Bootstrap/docs/assets/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="Bootstrap/docs/assets/js/ie10-viewport-bug-workaround.js"></script>
   
    <script>
	    console.log("doing on load script"); 
		var btnCreateMovie = document.getElementById("btnCreateMovie"); 
		var btnManageCategories = document.getElementById("btnManageCategories");    		    	
    		
		console.log("got btnAdmin");
		var UserType = '<%= session.getAttribute("UserType") %>';
		console.log("user type = " + UserType);
		
		if (UserType == "Admin")
		{
			btnCreateMovie.style.display = "block";
			btnManageCategories.style.display = "block";
		}
		else
		{
			btnManageCategories.style.display = "none";
			btnCreateMovie.style.display = "none";
		}
	</script>
  </body>
</html>


