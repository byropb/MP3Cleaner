<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!-- The modal  Wait message -->
<div id="myModal" class="modal" style='display: none;'>
 <!-- Modal content -->
 <div class="modal-content">
  <span class="close" onclick="showObject('myModal', false);">&times;</span>
  <p>Processing request, please wait..</p>
  <img src='..${sessionScope.mp3Context}/images/wait01.gif' width='40px' />
 </div>
</div>

<!-- Action Complete message  -->
<div id="actionmsg" class="modal" style='display: none'>
 <!-- Modal content -->
 <table class="modal-content">
  <tr>
   <td><span class="close" onclick="showObject('actionmsg', false); resetSrc('actionconfirm');">&times;</span></td>
  </tr>
  <tr>
   <td></td>
  </tr>
  <tr>
   <td id='actionconfirm' style='font-size: 12px; height: 60px; displayT: block; word-wrap: overflow-wrap;'></td>
  </tr>
  <tr>
   <td>&nbsp;</td>
  </tr>
 </table>
</div>

<!-- The Modal Edit confirmation  -->
<div id="editform" class="modal" style='display: none'>
 <!-- Modal content -->

 <table class="modal-content">
  <tr>
   <td colspan='3'><span class="close" onclick="showObject('editform', false);resetSrc('editname'); revert_edit();">&times;</span></td>
  </tr>
  <tr>
   <td colspan='3' class='header'>Do you want to save changes of following?</td>
  </tr>
  <tr>
   <td colspan='3' id='editname' style='font-size: 12px; heightT: 60px; word-wrap: overflow-wrap;'>file name goes
    here</td>
  </tr>
  <tr>
  <tr>
   <td colspan='3'>&nbsp;</td>
  </tr>
  <tr>
   <td><input type='button' class='button' value='Save'
    onclick='do_edit(document.getElementById("editname").innerHTML)'></td>
   <td>&nbsp;</td>
   <td><input type='button' class='button' value='Cancel'
    onclick="showObject('editform', false); resetSrc('editname'); revert_edit();"></td>
  </tr>
  <tr>
   <td colspan='3'></td>
  </tr>
 </table>
</div>

<!--  The Modal Delete - -->
<div id="deleteform" class="modal" style='display: none'>
 <!-- Modal content -->

 <table class="modal-content">
  <tr>
   <td colspan='3'><span class="close" onclick="showObject('deleteform', false); resetSrc('deletename');">&times;</span></td>
  </tr>
  <tr>
   <td colspan='3' class='header'>Do you want delete the file?</td>
  </tr>
  <tr>
   <td colspan='3' id='deletename' style='font-size: 12px; heightT: 60px; word-wrap: overflow-wrap;'>file name goes
    here</td>
  </tr>
  <tr>
  <tr>
   <td colspan='3'>&nbsp;</td>
  </tr>
  <tr>
   <td><input type='button' class='button' value='Delete'
    onclick='do_delete(document.getElementById("deletename").innerHTML)'></td>
   <td>&nbsp;</td>
   <td><input type='button' class='button' value='Cancel'
    onclick="showObject('deleteform', false); resetSrc('deletename');"></td>
  </tr>
  <tr>
   <td colspan='3'></td>
  </tr>
 </table>
</div>


<script type='text/javascript'>
	function get_delete(id) {

		var deletename = document.getElementById("deletename");
		if (null !== deletename) {
			deletename.innerHTML = id;
		} else {
			return false;
		}
		showObject('documentFrame', false);
		showObject('deleteform', true);
	}

	function do_delete(id) {

		var fileurl = getContextUrl().concat("/delete/").concat(id);
		ajaxGet(fileurl, "actionconfirm", path_onchange);
		showObject('deleteform', false);
		showObject('actionmsg', true);

	}

	var editdata = "";
	var editcontrol = null;

	function get_edit(id) { // assigned in server files list
		editcontrol = document.getElementById(id);
		editdata = editcontrol.innerHTML;
		editcontrol.contentEditable = 'true';
		editcontrol.onclick = null; // temp remove get_download()
		editcontrol.focus();
		editcontrol.addEventListener("blur", function() {
			stop_edit(id)
		});
	}

	function stop_edit(id) {
		var editcontrol = document.getElementById(id);
		editcontrol.contentEditable = 'false';
		editcontrol.setAttribute("onclick", "get_download('" + id + "')");
		var editname = document.getElementById("editname");
		if (editdata !== editcontrol.innerHTML.trim()) {
			editname.innerHTML = "rename '" + editdata + "' to '" + editcontrol.innerHTML + "'?"
			showObject('editform', true);
		}
	}
	function revert_edit() {
		editcontrol.innerHTML = editdata;
	}
	function do_edit(id) {
		var fileurl = getContextUrl().concat("/edit/").concat(editdata).concat("/").concat(editcontrol.innerHTML);
		ajaxGet(fileurl, "actionconfirm", path_onchange);
		showObject('editform', false);
	}

	//---

	function get_download(filename) {

		this.resetMedia();
		var filetype = "";
		var filedock = null;
		var container = document.getElementById("container");
		var fileurl = getContextUrl().concat("/download/").concat(filename);
		var lastIndex = filename.lastIndexOf("."); //get extention
		if (0 < lastIndex) {
			filetype = filename.substring(lastIndex + 1).toLowerCase();
		}
		switch (filetype) {
		case "mp3":
		case "wav":
			filedock = document.createElement("audio");
			filedock.style.height = '50%';
			filedock.controls = true;
			filedock.autoplay = true;
			filedock.volume = 0.5;
			filedock.src = fileurl;

			span = document.createElement("span");
			span.innerHTML = filename;
			span.style.color = "green";
			container.appendChild(span);
			container.appendChild(filedock);

			break;

		case "jpg":
		case "png":
		case "bmp":
			showObject('documentFrame', true); //modal_msg.jsp
			showObject('documentDock', false);
			showObject('imageDock', true);
			filedock = document.getElementById("imageDock");
			filedock.src = fileurl;

			break;
		case "mp4":
		case "avi":
		case "mov":
		case "pdf":
		case "txt":
			showObject('documentFrame', true); //modal_msg.jsp //document.getElementById("documentFrame") 
			showObject('documentDock', true);
			showObject('imageDock', false);
			filedock = document.getElementById('documentDock');
			filedock.src = fileurl;

			break;
		case "docx":
		case "doc":
		case "xlsx":
			showObject('documentFrame', false); //modal_msg.jsp //document.getElementById("documentFrame") 
			showObject('documentDock', false);
			showObject('imageDock', false);
			filedock = document.getElementById('documentDock');
			filedock.src = fileurl;
			break;

		case "":
			break;
		default:
			showObject('documentFrame', false); // download others
			showObject('documentDock', false);
			showObject('imageDock', false);
			filedock = document.getElementById('documentDock');
			filedock.src = fileurl;
			break;
		}
	}

	function resetMedia() {

		var filedock = document.getElementById("filedock");
		if (null !== filedock) {
			filedock.pause();
		}
		showObject('documentFrame', false); // located in popup_dock.jsp include
		showObject('documentDock', false);
		showObject('imageDock', false);
		resetSrc('container');
		resetSrc('documentDock');
		resetSrc('imageDock');
		var documentFrame = document.getElementById("documentFrame"); // located in popup_dock.jsp include
		documentFrame.style.width = "300px";
		documentFrame.style.height = "300px";
	}
</script>


