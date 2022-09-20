$(document).ready(function(){

	$('nav.navbar a, .scrollTop').click(function(event){
		if (this.hash !== "") {
			event.preventDefault();
			var hash = this.hash;
			$('section').removeClass("focus");
			$(hash).addClass("focus");
			setTimeout(function(){
				$(hash).removeClass("focus");
			}, 2000);
			$('html, body').animate({
				scrollTop: $(hash).offset().top - 69
			}, 600, function(){
				window.location.hash = hash;
			});
			$(".navbar-collapse").collapse('hide');
		}

	});
	$(window).scroll(function(){
		var scrollPos = $('body').scrollTop();
		if(scrollPos > 0){
			$('.navbar').addClass('show-color');
			$('.scrollTop').addClass('show-button');
		} else{
			$('.navbar').removeClass('show-color');
			$('.scrollTop').removeClass('show-button');
		}

	});
});
$(function(){
  $('.carousel-item').eq(0).addClass('active');
  var total = $('.carousel-item').length;
  var current = 0;
  $('#moveRight').on('click', function(){
    var next=current;
    current= current+1;
    setSlide(next, current);
  });
  $('#moveLeft').on('click', function(){
    var prev=current;
    current = current- 1;
    setSlide(prev, current);
  });
  function setSlide(prev, next){
    var slide= current;
    if(next>total-1){
     slide=0;
      current=0;
    }
    if(next<0){
      slide=total - 1;
      current=total - 1;
    }
           $('.carousel-item').eq(prev).removeClass('active');
           $('.carousel-item').eq(slide).addClass('active');
      setTimeout(function(){

      },800);
    console.log('current '+current);
    console.log('prev '+prev);
  }
});