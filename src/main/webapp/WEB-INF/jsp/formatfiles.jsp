<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MP3Cleaner Web</title>

<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${mp3Context}/js/mp3cleaner.js"></script>
</head>
<body onload="body_onload();">
 <jsp:include page="modal_msg.jsp"></jsp:include>
 <jsp:include page="popup_dock.jsp"></jsp:include>

 <form id='mp3cleaner' name='mp3cleaner' target="_self" method='post' action="javascript:submitFormPost();"
  onsubmit="form_onsubmit();">
  <table class='tab_frame' style='min-width: 600px'>
   <tr>
    <td style='text-align: center;'>
     <h3>Set MP3 Attributes</h3>(set attributes for all mp3 in current directory)
    </td>
   </tr>
   <tr>
    <td>
     <table class='tab_form'>
      <tr>
       <td class='label'>Directory<span class='req'>*</span>:
       </td>
       <td colspan='2'><input type='text' id='path' name='path' required='required' class='localpath'
        style='width: 90%' title='full path to local mp3 directory' value='${mp3Model.path}'
        onchange="path_onchange(); " placeholder='C:/<path to mp3 directory>' /> <img
        src='..${mp3Context}/images/refresh01.jpg' style='width: 15px; vertical-align: middle; cursor: pointer'
        onclick='path_onchange();' title='refresh directory view' /></td>
      </tr>

      <tr>
       <td class='label'>Album<span class='req'>*</span>:
       </td>
       <td><input type='text' id='bookName' required='required' name='bookName' placeholder='album or book name'
        title='album or book name' value='${mp3Model.bookName}' /> <img src='..${mp3Context}/images/refresh01.jpg'
        style='width: 15px; vertical-align: middle; cursor: pointer' onclick='get_mp3attributes();'
        title='refresh attributes view' /></td>
       <td rowspan='6'>
        <div id='container' style='width: 300px; height: 80px; text-align: center; overflow-x: auto; overflow-y: auto'>&nbsp;</div>
       </td>
      </tr>
      <tr>
       <td class='label'>Title<span class='req'>*</span>:
       </td>
       <td><input type='text' id='title' required='required' name='title' value='${mp3Model.title}'
        title='title/chapter name' placeholder='title/chapter name' /></td>
      </tr>
      <tr>
       <td class='label'>Author<span class='req'>*</span>:
       </td>
       <td><input type='text' id='author' required='required' name='author' value='${mp3Model.author}'
        title='contributing artist' placeholder='written by' /></td>
      </tr>
      <tr>
       <td class='label'>Read by<span class='req'>*</span>:
       </td>
       <td><input type='text' id='reader' required='required' name='reader' value='${mp3Model.reader}'
        title='album artist' placeholder='read by' /></td>
      </tr>
      <tr>
       <td class='label'>Year published:</td>
       <td><input type='number' id='year' name='year' required='required' value='${mp3Model.year}'
        title='year first published' placeholder='year first published' /></td>
      </tr>
      <tr>
       <td class='label'>Starting Sequence#:</td>
       <td><input type='number' id='startCount' required='required' title='titles/chapters starting index'
        name='startCount' value='${mp3Model.startCount}' placeholder='chapters start index' /></td>
      </tr>
      <tr>
       <td class='label'>Mp3 Image(opt.):</td>
       <td><input type='text' id='imageName' title="mp3 embedded image path (optional)" name='imageName'
        value='${mp3Model.imageName}' placeholder="mp3 embedded image.jpg" /></td>
       <td style='text-align: center'><input type='text' style='visibility: hidden' name='fileattributes'
        id='fileattributes' /></td>
      </tr>
      <tr>
       <td colspan='3'>
        <hr />
       </td>
      </tr>
      <tr>
       <td><input type='button' value='to dashboard'
        onclick="showObject('myModal', true); toDashboard('mp3cleaner');" class='button' title='go to dashboard' /></td>

       <td style='text-align: right'>
       <input type='button' onclick='cleanup();' value='remove attributes' style='width:120px'
        class='button' title='remove attributes for all mp3 files in directory' /></td>

       <td style='text-align: right'><input type='submit' value='set attributes' class='button'
        title='set attributes for all mp3 files in current directory' />
     </table>
    </td>
   </tr>
   <tr>
    <td colspan='1'>
     <hr />
    </td>
   </tr>
   <tr>
    <td colspan='1'>
     <div id='fileslist' style="overflow-y: scroll; max-height: 400px;"></div>

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

	// for read/write localStorage function
	const inputPath = [ "path" ];
	const inputsList = [ "bookName", "title", "author", "reader", "year", "executionTime" ];
	var myInterval = 0;

	function body_onload() {
		readStorage('mp3in_', inputPath);
		form_onsubmit();
		ajaxDataGet('/api/data_get?filetype=.mp3&dirPath=', 'path', 'fileslist', get_mp3attributes);
	}

	function path_onchange() {
		clearInputs(inputsList);
		resetSrc('container');
		writeStorage('mp3in_', inputPath);
		form_onsubmit();
		ajaxDataGet('/api/data_get?filetype=.mp3&dirPath=', 'path', 'fileslist', get_mp3attributes);
	}

	function get_mp3attributes() {
		ajaxDataGet('/api/data_attr?dirPath=', 'path', 'fileattributes', loadAttributes);
	}

	function cleanup() {
		var form = document.getElementById("mp3cleaner");
		form.action = "do_cleanup";
		form.submit();
	}

	function loadAttributes() {
		try { // example arrayList = ["path=C:/Data/Music_Books/OZ_History/audio/", "bookName=OZ_Story", "title=chapter_ 01", "author=Strugatsky B", "reader=Kokshov", "year=1987"]
			var list = document.getElementById("fileattributes").value;
			var jsArray = JSON.parse(list);
			for (var i = 0; i < jsArray.length; i++) {
				var attribute = jsArray[i].split("=");
				document.getElementById(attribute[0]).value = attribute[1];
			}
			cancelModal();
		} catch (e) {
			alert(e)
		}
	}

	function cancelModal() {
		showObject('myModal', false);
		startTimer("executionTime", false);
	}

	//---Form submit POST--//
	function submitFormPost() {
		this.resetMedia();
		var url = getContextUrl().concat("/do_ajax_format");
		ajaxFormPost(url, 'mp3cleaner', 'fileslist', cancelModal);
	}

	function submitJsonPost() {
		showObject('myModal', true);
		startTimer("executionTime", true);
		var url = getContextUrl().concat("/do_json_format");
		var jsondata = parseJsonData('mp3cleaner');
		ajaxJSonPost(url, jsondata, 'fileslist', cancelModal);
	}

	//---read form's data into JSON string//
	function parseJsonData(formid) {
		const myForm = document.getElementById(formid);
		//const myForm = document.getElementById('mp3cleaner');
		const formElements = myForm.elements;
		var jsonData = "{";
		for (let i = 0; i < formElements.length; i++) {
			const element = formElements[i];
			if ("text" === element.type) {
				if ("fileattributes" !== element.name) {
					var value = element.value.replaceAll(",", "").replaceAll("\"", "");
					jsonData = jsonData.concat("\"").concat(element.name).concat("\"").concat(":").concat("\"").concat(value)
							.concat("\",");
				}
			}
		}

		jsonData = jsonData.substring(0, jsonData.length - 1) + "}";
		return jsonData;
	}
	
 
	function get_download_bak(filename) {
		
		var mp3player = document.getElementById("mp3player");
		var mp3name =  document.getElementById("mp3name");
		var mp3url = getContextUrl().concat("/download/").concat(filename); 
		if (null !== mp3player){
			mp3name.innerHTML= filename;
			mp3player.src = mp3url;
			mp3player.volume = 0.5;
			mp3player.play();
		}
	}
	

	function resetMedia() {
		var filedock = document.getElementById("filedock");
		if(null !== filedock){
			filedock.pause();
		}
		resetSrc('container');
	}
 
  function downloadFile_example() { // where the download  requires implementaiton in web-controller. see annotation @GetMapping("/download/{filename:.+}")
      fetch("http://localhost:8082/mp3cleaner/download")
          .then(response => response.blob())
          .then(blob => {
              const url = window.URL.createObjectURL(blob);
              const a = document.createElement("a");
              a.href = url;
              a.download = "sample.pdf"; // optional override
              document.body.appendChild(a);
              a.click();
              a.remove();
          });
  }
 
</script>
</html>






