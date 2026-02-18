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
<style>
label {
	font-weight: normal;
}

#checkboxes label {
	display: block;
	font-size: 11px;
}

#checkboxes label:hover {
	background-color: #1e90ff;
}

#fieldoptions {
	positionT: absolute;
	top: 12%;
	left: 40%;
	background-color: #F2F2F2;
	border-style: solid 1px;
	border-spacing: 10px;
	border-radius: 25px;
	align-self: center;
	margin: 0 auto;
	widthTT: auto;
	min-width: 300px;
	display:;
}

#checkboxes {
	background-color: white;
}
</style>
</head>
<body onload="readStorage('mp3_', fieldslist);">
 <jsp:include page="modal_msg.jsp"></jsp:include>
 <form id='mp3cleaner' name='mp3cleaner' target="_self" method='get' action="submit">
  <table class='tab_frame'>
   <tr>
    <td style='text-align: center;'>
     <h3>MP3 Files Cleaner</h3>
    </td>
   </tr>
   <tr>
    <td>
     <table class='tab_form' style='width: 100%'>
      <tr>
       <td class='' colspan='2' style='text-align: center'>Please select an action to continue</td>
      </tr>
      <tr>
       <td class='label'>MP3 attributes:</td>
       <td><input type='radio' id='option' onclick="form_submit('show_format');" name='option' value='format' /></td>
      </tr>
      <tr>
       <td class='label'>Rename files:</td>
       <td><input type='radio' id='option' onclick="form_submit('show_rename');" name='option' value='rename' /></td>
      </tr>
      <tr>
       <td colspan='2'>
        <hr />
       </td>
      </tr>
      <tr>
       <td colspan='2' style='cursor: pointer; font-size: 12px; font-weight: bold'
        onclick="showObject('fieldoptions', true);">Options<img src='..${mp3Context}/images/gear.gif'
        style='width: 20px; vertical-align: middle;' />
       </td>
      </tr>
      <tr>
       <td colspan='2'>
        <table id='fieldoptions' style='display: none; position: fixed;' border=0>
         <tr>
          <td class='' style='text-align: center'>Select the details you want to display</td>
          <td class='close' onclick="showObject('fieldoptions', false);">&times;</td>
         </tr>
         <tr>
          <td class='' colspan='2'><label for="selectall"> <input type="checkbox" name="selectall"
            id="selectall" onchange='selectAll()' />select all
          </label></td>
         </tr>
         <tr>
          <td colspan='2'>
           <div>
            <div id="checkboxes">
             <label for="trak"><input type="checkbox" id="trackOption" name="trackOption" />Track </label> <label
              for="title"> <input type="checkbox" name="titleOption" id="titleOption" />Title
             </label> <label for="artist"> <input type="checkbox" name="artistOption" id="artistOption" />Artist
             </label> <label for="album"> <input type="checkbox" name="albumOption" id="albumOption" />Album
             </label> <label for="readby"> <input type="checkbox" name="readbyOption" id="readbyOption" />Read by
             </label> <label for="year"> <input type="checkbox" name="yearOption" id="yearOption" />Year
             </label> <label for="length"> <input type="checkbox" name="lengthOption" id="lengthOption" />Length
             </label> <label for="size"> <input type="checkbox" name="sizeOption" id="sizeOption" />Size
             </label> <label for="modified"> <input type="checkbox" name="modifiedOption" id="modifiedOption" />Last
              Modified
             </label>
            </div>
           </div>
          </td>
         </tr>
         <tr>
          <td class='' colspan='2' style='text-align:'><hr /></td>
         </tr>
         <tr>
          <td class='' colspan='2'>
           <table style='width: 100%'>
            <tr>
             <td class='' colspan='' style='text-align: center;'><input type='button' value='OK'
              style='width: 80px' onclick="writeStorage('mp3_',fieldslist); showObject('fieldoptions', false);" /></td>
             <td class='' colspan='' style='text-align: center;'><input type='button' value='Cancel'
              style='width: 80px' onclick="showObject('fieldoptions', false);" /></td>
            </tr>
           </table>
          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
    </td>
   </tr>
  </table>
 </form>
 <script type="text/javascript">
  
			const fieldslist = [ "trackOption", "titleOption", "artistOption",
					"albumOption", "readbyOption", "yearOption",
					"lengthOption", "sizeOption", "modifiedOption" ];

			function selectAll() {
				var checked = document.getElementById("selectall").checked;
				var icount = fieldslist.length;
				for (var i = 0; i < icount; i++) {
					var inputbox = document.getElementById(fieldslist[i]);
					inputbox.checked = checked;
				}
			}
			function form_submit(action) {
				var form = document.getElementById("mp3cleaner");
				if (null !== form) {
					showObject("myModal", true);
					form.action = action;
					form.submit();
				}
			}
		</script>
</body>
</html>