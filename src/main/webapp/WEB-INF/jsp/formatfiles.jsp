<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MP3Cleaner Web</title>
<link rel="stylesheet" type="text/css" href="..${sessionScope.mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${sessionScope.mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${sessionScope.mp3Context}/js/mp3cleaner.js"></script>
<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/mp3style.css" />
<link rel="stylesheet" type="text/css" href="..${mp3Context}/css/modal_msg.css" />
<script type="text/javascript" src="..${mp3Context}/js/mp3cleaner.js"></script>
</head>
<body onload="body_onload();">
  <jsp:include page="modal_msg.jsp"></jsp:include>
  <form id='mp3cleaner' name='mp3cleaner' target="_self" method='post' action="do_format" onsubmit="form_onsubmit();">
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
              <td colspan='2'><input type='text' id='path' name='path' required='required' class='localpath' style='width: 90%'
                title='full path to local mp3 directory' value='${mp3Model.path}' onchange="path_onchange(); "
                placeholder='C:/<path to mp3 directory>' /></td>
            </tr>
            <tr>
              <td class='label'>Album<span class='req'>*</span>:
              </td>
              <td><input type='text' id='bookName' required='required' name='bookName' placeholder='album or book name'
                title='album or book name' value='${mp3Model.bookName}' /> 
                
                <img src='..${mp3Context}/images/refresh01.jpg'
                style='width: 15px; vertical-align: middle; cursor: pointer' onclick='path_onchange()'
                title='get file attributes from the directory' />
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
              <td><input type='number' id='startCount' required='required' title='titles/chapters starting index' name='startCount'
                value='${mp3Model.startCount}' placeholder='chapters start index' /></td>
            </tr>
            <tr>
              <td class='label'>Mp3 Image(opt.):</td>
              <td><input type='text' id='imageName' title="mp3 embedded image path (optional)" name='imageName'
                value='${mp3Model.imageName}' placeholder="mp3 embedded image.jpg" /></td>
              <td style='text-align: center'><input type='text' style='visibility: hidden' name='fileattributes' id='fileattributes' />
              </td>
            </tr>
            <tr>
              <td colspan='3'>
                <hr />
              </td>
            </tr>
            <tr>
              <td><input type='button' value='to dashboard' onclick="showObject('myModal'); toDashboard('mp3cleaner');" class='button'
                title='go to dashboard' /></td>
              <td style='text-align: center'>&nbsp;<input type='button' onclick='cleanup();' value='remove attributes'
                title='remove attributes for all mp3 files in directory' /></td>
              <td style='text-align: right'><input type='submit' value='set attributes' class='button'
                title='set attributes for all mp3 files in files directory' /></td>
            </tr>
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
    </table>
  </form>
</body>
<script type="text/javascript">
	// for read/write localStorage function
	const inputPath = [ "path" ];
	const inputsList = [ "bookName", "title", "author", "reader", "year" ];

	function body_onload() {
		readStorage('mp3in_', inputPath);
		getAjaxData('/api/data_get?filetype=.mp3&dirPath=', 'path', 'fileslist', null);
	}
	function form_onsubmit() {
		showObject('myModal');
	}
	function path_onchange() {
		clearInputs(inputsList);
		writeStorage('mp3in_', inputPath);
		getAjaxData('/api/data_get?filetype=.mp3&dirPath=', 'path', 'fileslist', get_mp3attributes);
	}

	function get_mp3attributes() {
		getAjaxData('/api/data_attr?dirPath=', 'path', 'fileattributes', loadAttributes);
	}
    function cleanup(){
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

		} catch (e) {
			alert(e)
		}
	}
</script>
</html>
