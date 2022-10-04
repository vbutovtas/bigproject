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

class NotificationCenter {
	constructor() {
		this.items = [];
		this.itemsToKill = [];
		this.messages = NotificationMessages();
		this.killTimeout = 20;

		this.spawnNote();
	}
	spawnNote() {
		const id = this.random(0,2**32,true).toString(16);
		const draw = this.random(0,this.messages.length - 1,true);
		const message = this.messages[draw];
		const note = new Notification({
			id: `note-${id}`,
			icon: message.icon,
			title: message.title,
			subtitle: message.subtitle,
			actions: message.actions
		});
		const transY = 100 * this.items.length;

		note.el.style.transform = `translateY(${transY}%)`;
		note.el.style.zIndex = `582319`;
		note.el.addEventListener("click",this.killNote.bind(this,note.id));

		this.items.push(note);
	}
	killNote(id,e) {
		const note = this.items.find(item => item.id === id);
		const tar = e.target;

		if (note && tar.getAttribute("data-dismiss") === id) {
			note.el.classList.add("notification--out");
			this.itemsToKill.push(note);

			clearTimeout(this.killTimeout);

			this.killTimeout = setTimeout(() => {
				this.itemsToKill.forEach(itemToKill => {
					document.body.removeChild(itemToKill.el);

					const left = this.items.filter(item => item.id !== itemToKill.id);
					this.items = [...left];
				});

				this.itemsToKill = [];

				this.shiftNotes();
			},note.killTime);
		}
	}
	shiftNotes() {
		this.items.forEach((item,i) => {
			const transY = 100 * i;
			item.el.style.transform = `translateY(${transY}%)`;
		});
	}
	random(min,max,round = false) {
		const percent = crypto.getRandomValues(new Uint32Array(1))[0] / 2**32;
		const relativeValue = (max - min) * percent;

		return min + (round === true ? Math.round(relativeValue) : +relativeValue.toFixed(2));
	}
}

class Notification {
	constructor(args) {
		this.args = args;
		this.el = null;
		this.id = null;
		this.killTime = 20;
		this.init(args);
	}
	init(args) {
		const {id,icon,title,subtitle,actions} = args;
		const block = "notification";
		const parent = document.body;
		const xmlnsSVG = "http://www.w3.org/2000/svg";
		const xmlnsUse = "http://www.w3.org/1999/xlink";

		const note = this.newEl("div");
		note.id = id;
		note.className = block;
		parent.insertBefore(note,parent.lastElementChild);

		const box = this.newEl("div");
		box.className = `${block}__box`;
		note.appendChild(box);

		const content = this.newEl("div");
		content.className = `${block}__content`;
		box.appendChild(content);

		const _icon = this.newEl("div");
		_icon.className = `${block}__icon`;
		content.appendChild(_icon);

		const iconSVG = this.newEl("svg",xmlnsSVG);
		iconSVG.setAttribute("class",`${block}__icon-svg`);
		iconSVG.setAttribute("role","img");
		iconSVG.setAttribute("aria-label",icon);
		iconSVG.setAttribute("width","32px");
		iconSVG.setAttribute("height","32px");
		_icon.appendChild(iconSVG);

		const iconUse = this.newEl("use",xmlnsSVG);
		iconUse.setAttributeNS(xmlnsUse,"href",`#${icon}`);
		iconSVG.appendChild(iconUse);

		const text = this.newEl("div");
		text.className = `${block}__text`;
		content.appendChild(text);

		const _title = this.newEl("div");
		_title.className = `${block}__text-title`;
		_title.textContent = title;
		text.appendChild(_title);

		if (subtitle) {
			const _subtitle = this.newEl("div");
			_subtitle.className = `${block}__text-subtitle`;
			_subtitle.textContent = subtitle;
			text.appendChild(_subtitle);
		}

		const btns = this.newEl("div");
		btns.className = `${block}__btns`;
		box.appendChild(btns);

		actions.forEach(action => {
			const btn = this.newEl("button");
			btn.className = `${block}__btn`;
			btn.type = "button";
			btn.setAttribute("data-dismiss",id);

			const btnText = this.newEl("span");
			btnText.className = `${block}__btn-text`;
			btnText.textContent = action;

			btn.appendChild(btnText);
			btns.appendChild(btn);
		});

		this.el = note;
		this.id = note.id;
	}
	newEl(elName,NSValue) {
		if (NSValue)
			return document.createElementNS(NSValue,elName);
		else
			return document.createElement(elName);
	}
}

function NotificationMessages() {
	return [
		{
			icon: "success",
			title: "Request accepted for processing. Thanks",
			actions: ["OK"]
		}
	];
}