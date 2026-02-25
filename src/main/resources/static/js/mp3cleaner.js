
//---
var xhr = new XMLHttpRequest();

function getAjaxData(serviceid, controlid, dockid, nextevent) {  // getAjaxData('/api/data_get?dirPath=', 'path',  'fileslist');

	var url = getContextUrl().concat(serviceid);
	var data = document.getElementById(controlid).value.toString();
	var data_split = data.split("\\"); // replace backslash to forward slash in path
	var data_join = data_split.join("/"); // replace backslash to forward slash in path
	var lastChar = data_join.charAt(data_join.length - 1).toString();
	if ("/" != lastChar) { // if last char is not slash, add trailing slash to the  path
		data_join = data_join.concat("/");
	}
	document.getElementById(controlid).value = data_join.toString(); // restore value in path input box
	url = url.concat(data_join);
	ajaxGet(url, dockid, nextevent);
	// url = http://localhost:8082/mp3cleaner/api/data_get?dirPath=C:/Data/Music_Books/OZ_History/audio/
}

function form_onsubmit() {
	showObject('myModal', true);
	startTimer("executionTime", true);
}
function ajaxDataGet(serviceid, controlid, dockid, event) {
	var url = getContextUrl().concat(serviceid);
	var data = parseSourcePath('path'); //format sting path to mp3 directory
	url = url.concat(data);
	ajaxGet(url, dockid, event);
}
//--
function getContextUrl() {
	var location = window.location.href;
	var contextUrl = location.substring(0, location.lastIndexOf("/"));
	return contextUrl;
}
function parseSourcePath(controlid) {

	var data = document.getElementById(controlid).value.toString();
	if (2 > data.length) {
		document.getElementById(controlid).value = "";
		return "";
	}
	var data_split = data.split("\\"); // replace backslash to forward slash in path
	var data_join = data_split.join("/"); // replace backslash to forward slash in path
	var lastChar = data_join.charAt(data_join.length - 1).toString();
	if ("/" != lastChar) { // if last char is not slash, add trailing slash to the  path
		data_join = data_join.concat("/");
	}
	document.getElementById(controlid).value = data_join.toString();
	return data_join;
}
//--

function ajaxGet(url, dockid, nextevent) { // call returns directory files lising ajaxGet(url, 'fileslist')

	xhr.onreadystatechange = function() {
		ajaxStatusChange(dockid, nextevent);
	}
	//alert(url)
	xhr.open('GET', url, true);
	xhr.send();
}

function ajaxFormPost(url, formid, dockid, nextevent) {

	xhr.onreadystatechange = function() {
		ajaxStatusChange(dockid, nextevent);
	}
	var form = document.getElementById(formid);
	var formData = new FormData(form);

	xhr.open("POST", url, true);
	xhr.send(formData);
}

function ajaxJSonPost(url, jsondata, dockid, nextevent) {

	xhr.onreadystatechange = function() {
		ajaxStatusChange(dockid, nextevent);
	}
	xhr.open("POST", url, true);
	xhr.send(jsondata);
}

function ajaxStatusChange(dockid, nextevent) { // when ajax status changed, fire nextevent (function) with time delay 500ms

	if (xhr.readyState === 4 && xhr.status === 200) {
		if ("text" === document.getElementById(dockid).type) { //input type="text"
			document.getElementById(dockid).value = xhr.responseText;
		} else {
			document.getElementById(dockid).innerHTML = xhr.responseText;
		}
		if (null !== nextevent) {
			setTimeout(nextevent, 500);
		}
	} else if (xhr.readyState === 4 && xhr.status !== 200) {
		//console.error('Error:', xhr.status, xhr.statusText);
		if (0 === xhr.status) {
			var msg = "<span class='header'>The request rejected by the Server</span><br/> Possible reason: The file is too large or type is invalid!";
			this.showServiceMsg('actionmsg', 'actionconfirm', msg);
		}
	}
}

function showServiceMsg(msgcontainer, msgform, msg) {
	var form = document.getElementById(msgform);
	if (null !== form) {
		form.innerHTML = msg;
		showObject(msgcontainer, true);
	}
}

function toDashboard(formid) {
	var form = document.getElementById(formid);
	if (null !== form) {
		form.method = "GET";
		form.action = "show_dashboard";
		form.submit();
	} else {
		alert("undefined object: " + formid);
	}
}

function showObject(id, action) { // show/hide html object
	var object = document.getElementById(id);
	if (null !== object) {
		if (true === action) {
			object.style.display = "";
		} else {
			object.style.display = "none";
			//object.innerHTML = "";
		}
	} else {
		alert("undefined object: " + id);
	}
}


function resetSrc(id) { // set empty object's innerHTML property
	var object = document.getElementById(id);
	if (null !== object) {
		object.src = null;
		object.innerHTML = "";
	} else {
		alert("undefined object: " + id);
	}
}

function disableObject(id, action) {

	var object = document.getElementById(id);
	if (null !== object) {
		object.disabled = action;
	} else {
		alert("undefined object: " + id);
	}
}
function clearInputs(inList) { // example: list of input id, const inputsList = [ "bookName", "title", "author", "reader", "year" ];
	var icount = inList.length;
	for (var i = 0; i < icount; i++) {
		var inputbox = document.getElementById(inList[i]);
		if (null !== inputbox) {
			if ("checkbox" === inputbox.type || "radio" === inputbox.type) {
				inputbox.checked = null;
			}
			if ("text" === inputbox.type || "textarea" === inputbox.type) {
				inputbox.value = "";
			}
			if ("number" === inputbox.type) {
				inputbox.value = "0";
			}
			if (null !== inputbox.innerHTML) {
				inputbox.innerHTML = "";
			}
		}
	}
}

function writeStorage(prefix, inList) {
	if (typeof (Storage) !== "undefined") {
		// used for checkboxes true/false
		var icount = inList.length;
		for (var i = 0; i < icount; i++) {
			var inputbox = document.getElementById(inList[i]);
			if ("checkbox" === inputbox.type || "radio" === inputbox.type) {
				localStorage.setItem(prefix.concat(inputbox.id),
					inputbox.checked);
			} else {
				localStorage.setItem(prefix.concat(inputbox.id), inputbox.value);
			}
		}
	}
}

function readStorage(prefix, inList) {
	var icount = inList.length;
	if (typeof (Storage) !== "undefined") {
		for (var i = 0; i < icount; i++) {
			var inputbox = document.getElementById(inList[i]);
			var inputData = localStorage.getItem(prefix.concat(inputbox.id));
			if ("checkbox" === inputbox.type || "radio" === inputbox.type) {
				inputbox.checked = null
				if ("true" === inputData) {
					inputbox.checked = inputData;
				}
				if (null === inputData) { // first time loaded
					document.getElementById("fieldoptions").style.display = "";
				}
			} else {
				inputbox.value = inputData;
			}
		}
	}
}

function cleartorage(prefix, inList) {
	if (typeof (Storage) !== "undefined") {
		// used for checkboxes true/false
		var icount = inList.length;
		for (var i = 0; i < icount; i++) {

			var inputbox = document.getElementById(inList[i]);
			if ("checkbox" === inputbox.type) {
				inputbox.checked = null
				localStorage.setItem(prefix.concat(inputbox.id), null);
			} else {
				inputbox.value = null;
				localStorage.setItem(prefix.concat(inputbox.id), "");
			}
		}
	}
}

function startTimer(dockid, action) {
	if (true === action) {
		const element = document.getElementById(dockid);
		if (null !== element) {
			element.innerHTML = "0";
			myInterval = setInterval(function() {
				element.innerHTML = (parseFloat(element.innerHTML) + 0.1).toFixed(1); // format 6.8 in sec
			}, 100);
		}
	} else {
		clearInterval(myInterval);
	}
}
//---

var sortdir = true;
function sortTable(tableName, columnId) { //re-order table rows by selected column

	var table, currentRow, nextRow, breakpoint;
	var bnext = true;
	table = document.getElementById(tableName);

	while (bnext) {
		bnext = false;
		var tabRows = table.rows;
		var count = tabRows.length;

		for (i = 0; i < count - 1; i++) {
			breakpoint = false;
			currentRow = tabRows[i].getElementsByTagName('TD')[columnId];
			nextRow = tabRows[i + 1].getElementsByTagName('TD')[columnId];
			if (null == currentRow) {
				continue;
			}
			var celval = currentRow.innerHTML.toLowerCase().replaceAll(":", "").replaceAll(",", ""); //exclude time and kb formats
			var celnextval = nextRow.innerHTML.toLowerCase().replaceAll(":", "").replaceAll(",", "");
			if (false == sortdir) { //sort acs
				if (!isNaN(parseInt(celval)) && !isNaN(parseInt(celnextval))) {
					if (parseInt(celval, 10) > parseInt(celnextval, 10)) {
						breakpoint = true;
						break;
					}
				} else {
					if (celval > celnextval) {
						breakpoint = true;
						break;
					}
				}
			} if (true == sortdir) { //sort desc
				if (!isNaN(parseInt(celval)) && !isNaN(parseInt(celnextval))) {
					if (parseInt(celval, 10) < parseInt(celnextval, 10)) {
						breakpoint = true;
						break;
					}
				} else {
					if (celval < celnextval) {
						breakpoint = true;
						break;
					}
				}
			}
		}
		if (breakpoint) {
			tabRows[i].parentNode.insertBefore(tabRows[i + 1], tabRows[i]);
			bnext = true;
		}
	}
	sortdir = !sortdir;
}


//--------------------------------------------------

function ajaxPost() {
	//const myData = { name: 'John Doe', age: 30 };
	//postDataWithXHR('/api/submit-form', myData);

	//var location = window.location.href; //http://localhost:8082/mp3cleaner/
	var url = getContextUrl().concat("/api/data");
	var data = document.getElementById("path").value;
	xhr.open('POST', url, true); // true for asynchronous
	xhr.setRequestHeader('Content-Type', 'application/json'); // Example for JSON data

	xhr.onload = function() {
		//alert("my Data onload()")
		if (xhr.status >= 200 && xhr.status < 300) {
			console.log('Success:', xhr.responseText);
		} else {
			console.error('Error:', xhr.statusText);
		}
	};

	xhr.onerror = function() {
		console.error('Request failed');
		alert('Request failed')
	};
	xhr.send(JSON.stringify(data)); // Send data as JSON string
}
//------------------------------HTML dynamic--------------------------------------


var xdiv = null; // object to be dragged or resized by setDrag and setResize functions
var clx = 0;
var cly = 0;

function setDrag(e, id) {  //onmousedown=\"setDrag(event,'cpecustomer')\" onmouseup=\"resetDrag(event,'cpecustomer')\
	try {
		xdiv = document.getElementById(id);
		if (null === xdiv) { return false; }
		xdiv.addEventListener('mousemove', doDrag);
		cly = e.clientY - parseInt(xdiv.style.top);
		clx = e.clientX - parseInt(xdiv.style.left);
		xdiv.style.cursor = "move";
	} catch (error) {
		alert(error)
	}
}

function resetDrag(e, id) {
	try {
		xdiv = document.getElementById(id);
		if (null === xdiv) { return false; }
		xdiv.removeEventListener('mousemove', doDrag);
		xdiv.style.cursor = "default";
		xdiv = null;
		isscrollable = false;
	} catch (error) {
		alert(error)
	}
}

function doDrag(e) {
	if (xdiv === null) { return false; }
	if (e === null) { return false; }
	try {
		xdiv.style.top = (e.clientY - cly) + "px";
		xdiv.style.left = (e.clientX - clx) + "px";
		xdiv.style.cursor = "move";
	} catch (error) {
		xdiv.style.cursor = "default";
		return false;
	}
	return true;
}
//----Resize----
//
// var xdiv = null;
// var clx = 0;
// var cly = 0;

function doResize(e) {

	if (null === xdiv) { return false; }
	if ((e.clientX - parseInt(xdiv.style.left)) >= 0) {
		xdiv.style.width = (e.clientX - parseInt(xdiv.style.left)) + 5 + "px";
	}
	if ((e.clientY - parseInt(xdiv.style.top)) >= 0) {
		xdiv.style.height = (e.clientY - (parseInt(xdiv.style.top))) + 5 + "px";
	}
	return true;
}

function setResize(e, id) {

	document.addEventListener("mousemove", doResize);
	document.addEventListener("mouseup", resetResize);
	xdiv = document.getElementById(id);

	if (null === xdiv) { return false; }
	cly = e.clientY - parseInt(xdiv.style.top);
	clx = e.clientX - parseInt(xdiv.style.left);
}
function resetResize(e, id) {

	document.removeEventListener("mousemove", doResize);
	document.removeEventListener("mouseup", resetResize);
	xdiv = document.getElementById(id);
	if (null === xdiv) { return false; }
	xdiv.style.cursor = "default";
	xdiv = null;
}

//-------------------------
