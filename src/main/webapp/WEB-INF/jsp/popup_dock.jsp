
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<style>
</style>
<!-- Modal content -->
<table class="modal-content" id="documentFrame"
 style='position: absolute; z-index: 50; top: 50px; left: 300px; min-width: 200px; width: 300px; height: 300px; display: none;'>
 <tr style='background-color: #EDEBEB; cursor: move;'>
  <td onmousedown="setDrag(event,'documentFrame')" onmouseup="resetDrag(event,'documentFrame')"></td>
  <td align='center' class='close'
   style='font-weight: bold; font-size: 20px; float: none; cursor: pointer; width: 20px; height: 20px;'
   onclick="showObject('documentFrame', false); resetSrc('documentDock');resetSrc('imageDock'); ">&times;</td>
 </tr>
 <tr>
  <td colspan='2'><iframe id="documentDock" src='' style='width: 100%; height: 100%'></iframe></td>
 </tr>
 <tr>
  <td colspan='2'><img id="imageDock" src='' style='width: 100%; height: 100%' /></td>
 </tr>
 <tr>
  <td></td>
  <td onmousedown="setResize(event,'documentFrame')" onmouseup="resetResize(event,'documentFrame');"
   style='cursor: se-resize; font-size:; width: 3px; height: 15px; border: #C8C8C8; border-bottom-width: 2px; border-right-width: 2px; border-right-style: solid; border-bottom-style: solid;'></td>
 </tr>
</table>
