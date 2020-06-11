$(document).ready(function(){
	
	$('a[href = "#odjava"]').click(function(){
		
		$.ajax({
			url : '../PocetniREST/rest/logout',
			type : 'GET',
			success : function(){
				alert('Vasa sesija je istekla!');
				window.location = './index.html';
			}
		});
	});
	
})