let SearchedWord= "";

console.log(SearchedWord +  "  din word");
let twords = document.getElementById("id1").innerHTML;

let splitted = twords.split(" ");

for (var i = 0; i < splitted.length; i++) {

	if (splitted[i] === SearchedWord)
		splitted[i].bold();
			console.log(splitted[i] + "din split ")

}

function saveWord(event){
	SearchedWord = event;
	console.log(SearchedWord + "   SearchedWord  ")
	
}
