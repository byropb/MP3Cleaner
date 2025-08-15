<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!-- The Modal -->
<div id="myModal" class="modal" style='display: none'>
  <!-- Modal content -->
  <div class="modal-content">
    <span class="close" onclick="showObject('myModal');">&times;</span>
    <p>Processing request, please wait..</p>
    <img src='..${sessionScope.mp3Context}/images/wait01.gif' width='40px' />
  </div>

</div>