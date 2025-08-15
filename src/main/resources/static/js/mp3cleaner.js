
//---
const xhr = new XMLHttpRequest();

function getAjaxData(serviceid, controlid, dockid, event) {  // getAjaxData('/api/data_get?dirPath=', 'path',  'fileslist');

	var location = window.location.href;
	var url = location.substring(0, location.lastIndexOf("/")).concat(serviceid);
	//var url = location.substring(0, location.lastIndexOf("/")).concat("/api/data_get?dirPath="); 
	var data = document.getElementById(controlid).value.toString();
	var data_split = data.split("\\"); // replace backslash to forward slash in path
	var data_join = data_split.join("/"); // replace backslash to forward slash in path
	var lastChar = data_join.charAt(data_join.length - 1).toString();
	if ("/" != lastChar) { // if last char is not slash, add trailing slash to the  path
		data_join = data_join.concat("/");
	}
	document.getElementById(controlid).value = data_join.toString(); // restore value in path input box
	url = url.concat(data_join);
	ajaxGet(url, dockid, event);
	// url = http://localhost:8082/mp3cleaner/api/data_get?dirPath=C:/Data/Music_Books/OZ_History/audio/
}


function ajaxGet(url, dockid, event) { // call returns directory files lising ajaxGet(url, 'fileslist')

	xhr.onreadystatechange = function() {
		ajaxStatusChange(dockid, event);
	}
	//alert(url)
	xhr.open('GET', url, true);
	xhr.send();
}
function ajaxStatusChange(dockid, event) {

	if (xhr.readyState === 4 && xhr.status === 200) {
		if ("text" === document.getElementById(dockid).type) { //input type="text"
			document.getElementById(dockid).value = xhr.responseText;
		} else {
			document.getElementById(dockid).innerHTML = xhr.responseText;
		}
		if (null !== event) {
			setTimeout(event, 500);
		}

	} else if (xhr.readyState === 4 && xhr.status !== 200) {
		// Request completed but with an error
		console.error('Error:', xhr.status, xhr.statusText);
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

function showObject(id) {
	var object = document.getElementById(id);
	if (null !== object) {
		var show = object.style.display;
		if ("none" === show) {
			object.style.display = "";
		} else {
			object.style.display = "none";
		}
	} else {
		alert("undefined object id: " + id);
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

//---

var sortdir = true;
function sortTable(tableName, columnId) {

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
			var celval = currentRow.innerHTML.toLowerCase().replaceAll(":","").replaceAll(",",""); //exclude time and kb formats
			var celnextval = nextRow.innerHTML.toLowerCase().replaceAll(":","").replaceAll(",","");
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

	var location = window.location.href; //http://localhost:8082/mp3cleaner/
	var url = location.substring(0, location.lastIndexOf("/")).concat(
		"/api/data");
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
