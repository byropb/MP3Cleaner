
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>MP3Cleaner Web UI</title>

<link rel="stylesheet" type="text/css" href="..${sessionScope.mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${sessionScope.mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${sessionScope.mp3Context}/js/mp3cleaner.js"></script>

<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${mp3Context}/js/mp3cleaner.js"></script>

</head>
<body onload="body_onload();">
 <jsp:include page="modal_msg.jsp"></jsp:include>

 <form id='mp3cleaner' name='mp3cleaner' target="_self" method='post' action="javascript:submitFormPost();"
  actionT="do_rename" onsubmit="form_onsubmit();" th:object="${mp3Model.extensions}">
  <table class='tab_frame'>
   <tr>
    <td style='text-align: center;'>
     <h3>Rename Files</h3> (change files name in current directory)
    </td>
   </tr>
   <tr>
    <td>
     <table class='tab_form'>
      <tr>
       <td class='label'>Directory:</td>
       <td colspan='2'><input type='text' id='path' name='path' required='required'
        title='enter the full path to the local file directory' style='width: 95%' class='localpath'
        value='${mp3Model.path}' onchange="path_onchange();" placeholder='C:/<path to files directory>' /></td>
      </tr>
      <tr>
       <td class='label'>Files type:</td>
       <td><select name="filetype" id="filetype" required='required' style='width: 30%' onchange="path_onchange();">
         <option>${mp3Model.dropDownTypes}</option>
       </select></td>
      </tr>
      <tr>
       <td class='label'>New file name:</td>
       <td><input type='text' id='newFileName' required='required' name='newFileName' id='newFileName'
        title='new file sequence name' placeholder='new files sequence name' value='${mp3Model.newFileName}' /></td>
      </tr>
      <tr>
       <td class='label'>Starting Sequence#:</td>
       <td><input type='number' id='startCount' name='startCount' required='required' style='width: 30%'
        title='Starting index of file sequence' value='${mp3Model.startCount}'
        placeholder='Starting index of file sequence' /></td>
      </tr>
      <tr>
       <td colspan='3'>
        <hr />
       </td>
      </tr>
      <tr>
       <td><input type='button' value='to dashboard'
        onclick="showObject('myModal', true); toDashboard('mp3cleaner');" class='button' title='go to dashboard' /></td>

       <td style='text-align: center'>&nbsp;</td>

       <td style='text-align: right'><input type='submit' title='rename all files in directory'
        value='rename files' class='button' /></td>
      </tr>
     </table>
    </td>
   </tr>
   <tr>
    <td colspan='1'>
     <div id='fileslist' style="overflow-y: scroll; max-height: 400px;">
      <jsp:include page="fileslist.jsp"></jsp:include>
     </div>
    </td>
   </tr>
   <tr>
    <td colspan='1' align='center'><div id='dock'>
      Execution time: <span id='executionTime'>0</span> sec.
     </div></td>
   </tr>
  </table>
 </form>
</body>
<script type="text/javascript">
	const inputPath = [ "path" ];

	function body_onload() {
		readStorage('mp3in_', inputPath);
		var filetype = document.getElementById("filetype").value;
		var serviceId = "/api/data_get?callerid=rename&filetype=".concat(filetype).concat("&dirPath=");
		form_onsubmit();
		ajaxDataGet(serviceId, 'path', 'fileslist', cancelModal);
	}

	function form_onsubmit() {
		showObject('myModal', true);
		startTimer("executionTime", true);
	}

	function path_onchange() {
		var filetype = document.getElementById("filetype").value;
		var serviceId = "/api/data_get?callerid=rename&filetype=".concat(filetype).concat("&dirPath=");
		writeStorage('mp3in_', inputPath);
		form_onsubmit()
		ajaxDataGet(serviceId, 'path', 'fileslist', cancelModal);
	}

	function ajaxDataGet(serviceid, controlid, dockid, event) {
		var url = getContextUrl().concat(serviceid);
		var data = parseSourcePath('path'); //format sting path to mp3 directory
		url = url.concat(data);
		ajaxGet(url, dockid, event);
	}

	//---Form submit POST--/
	function submitFormPost() {
		var url = getContextUrl().concat("/do_rename");
		ajaxFormPost(url, 'mp3cleaner', 'fileslist', cancelModal);
	}

	function cancelModal() {
		showObject('myModal', false);
		startTimer("executionTime", false);
	}
</script>
</html>