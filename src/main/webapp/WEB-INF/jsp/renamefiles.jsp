
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>MP3Cleaner Web UI</title>


<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${mp3Context}/js/mp3cleaner.js"></script>

</head>
<body onload="body_onload();">
 <jsp:include page="modal_msg.jsp"></jsp:include>
 <jsp:include page="popup_dock.jsp"></jsp:include>

 <form id='mp3cleaner' name='mp3cleaner' target="_self" method='post' action="javascript:submitFormPost();"
  onsubmit="form_onsubmit();" enctype="multipart/form-data">
  <table class='tab_frame' style='min-width: 600px'>
   <tr>
    <td style='text-align: center;'>
     <h3>Manage Files</h3> (change files name in current directory)
    </td>
   </tr>
   <tr>
    <td>
     <table class='tab_form'>
      <tr>
       <td class='label'>Directory:</td>
       <td colspan='2'><input type='text' id='path' name='path' required='required' class='localpath'
        title='enter the full path to the local file directory' style='width: 90%' value='${mp3Model.path}'
        onchange="path_onchange();" placeholder='C:/<path to files directory>' /> <img
        src='..${mp3Context}/images/refresh01.jpg' style='width: 15px; vertical-align: middle; cursor: pointer'
        onclick='path_onchange();' title='refresh directory view' /></td>
      </tr>
      <tr>
       <td class='label'>Files type:</td>
       <td><select name="filetype" id="filetype" required='required' style='width: 30%' onchange="path_onchange();">
         <option>${mp3Model.dropDownTypes}</option>
       </select></td>
       <td rowspan='3'>
        <div id='container' style='width: 300px; height: 80px; text-align: center; overflow-x: auto; overflow-y: auto'>&nbsp;</div>

       </td>
      </tr>
      <tr>
       <td class='label'>New file name:</td>
       <td><input type='text' id='newFileName' required='required' name='newFileName' id='newFileName'
        title='new file sequence name' placeholder='new files sequence name' value='${mp3Model.newFileName}' /></td>
      </tr>
      <tr>
       <td class='label' nowrap='nowrap'>Starting Sequence#:</td>
       <td><input type='number' id='startCount' name='startCount' required='required' style='width: 30%'
        title='Starting index of file sequence' value='${mp3Model.startCount}'
        placeholder='Starting index of file sequence' /></td>
      </tr>
      <tr>
       <td colspan='3'>
       
        <table style='padding: 10px; width:100%'>
         <tr>
          <td><input type='button' value='to dashboard'
           onclick="showObject('myModal', true); toDashboard('mp3cleaner');" class='button' title='go to dashboard' /></td>

          <td nowrap='nowrap' style='text-align:;'>
          <input type='file' style='max-width: 230px'
           onchange='fileSelected("uploadfile", "submitfile");' id='uploadfile' name='uploadfile' multiple="multiple"
           accept="application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint,text/plain, application/pdf, image/*, audio/*, video/*" />

           &nbsp; <input type='button' value='Upload' id='submitfile' onclick='do_upload()' style='display: none' class='button'/></td>

          <td style='text-align: right'><input type='submit' title='rename all files in directory'
           onclickT='submitFormPost()' value='rename all' class='button' /></td>
         </tr>
        </table>
        
       </td>
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
    <td colspan='1' align='center'><div>
      Execution time: <span id='executionTime'>0</span> sec.
     </div></td>
   </tr>

  </table>
 </form>
 

 
 
</body>
<script type="text/javascript">
	const inputPath = [ "path","filetype" ];
	var myInterval = 0;

	function body_onload() {
		readStorage('mp3in_', inputPath);
		var filetype = document.getElementById("filetype").value;
		var serviceId = "/api/data_get?callerid=rename&filetype=".concat(filetype).concat("&dirPath=");
		form_onsubmit();
		ajaxDataGet(serviceId, 'path', 'fileslist', cancelModal);
	}

	function path_onchange() {
		var filetype = document.getElementById("filetype").value;
		var serviceId = "/api/data_get?callerid=rename&filetype=".concat(filetype).concat("&dirPath=");
		writeStorage('mp3in_', inputPath);
		resetMedia();
		form_onsubmit();
		ajaxDataGet(serviceId, 'path', 'fileslist', cancelModal);

	}
	//---Form submit POST--/
	function submitFormPost() {
		resetMedia();
		//form_onsubmit();
		var url = getContextUrl().concat("/do_rename");
		ajaxFormPost(url, 'mp3cleaner', 'fileslist', cancelModal);
	}

	function cancelModal() {
		showObject('myModal', false);
		startTimer("executionTime", false);
	}

	//--- upload file

	function fileSelected(fileid, submitid) {
		var fileInput = document.getElementById(fileid);
		if (fileInput.files.length === 0) {
			document.getElementById(submitid).style.display = "none";
		} else {
			document.getElementById(submitid).style.display = "";
		}
	}
	function do_upload(){
		var url = getContextUrl().concat("/upload");
		ajaxFormPost(url, 'mp3cleaner', 'actionconfirm', path_onchange);
		showObject('actionmsg', true);
		document.getElementById("uploadfile").value = "";
		fileSelected("uploadfile", "submitfile");
		//setTimeout("showObject('actionmsg', false)",3000);
	}
</script>

</html>