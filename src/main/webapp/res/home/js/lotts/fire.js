var fireEffectCount = true;
var timeout;

function fireEffect() {

	// Set canvas drawing surface
	var space = document.querySelector("#surface");
	var target = document.querySelector(".fireBox");
	var surface = space.getContext("2d");
	surface.scale(1,1);

	// Set Particles
	var particles = [];
	var particle_count = 400;//fire count
	for(var i = 0; i < particle_count; i++) {
		particles.push(new particle());
	}
	// Set wrapper and canvas items size
	var canvasWidth = target.offsetWidth + 10;
	var canvasHeight = canvasWidth * 2 ;
	//$(".wrapper").css({width:canvasWidth,height:canvasHeight});
	$("#surface").css({width:canvasWidth,height:canvasHeight});

  	// shim layer with setTimeout fallback from Paul Irish
	//window.requestAnimFrame = (function(){
	//    return  window.requestAnimationFrame       ||
	//            window.webkitRequestAnimationFrame ||
	//            window.mozRequestAnimationFrame    ||
	//            function( callback ){
	//            	window.setTimeout(callback, 6000 / 60);
	//            };
	//})();
	getAnimateFrame();

	function particle() {
     
		this.speed = {x: -1+Math.random()*2, y: -5+Math.random()*5};
    	canvasWidth = (document.querySelector("#surface").width);
    	canvasHeight= (document.querySelector("#surface").height);

    	this.location = firePos(canvasWidth, canvasHeight);
    	// this.location = {x: canvasWidth/2, y: (canvasHeight/2)+35};

		this.radius = .5 + Math.random() * 10;

		this.life = 15 + Math.random() * 10;
		this.death = this.life;

		this.r = 255;
		this.g = Math.round(Math.random()*155);
		this.b = 0;
	}
  
	function ParticleAnimation(){
		surface.globalCompositeOperation = "source-over";
		// surface.fillStyle = "black";//set background
		// surface.fillRect(0, 0, canvasWidth, canvasHeight);//set background
		surface.clearRect(0, 0, canvasWidth, canvasHeight);//clear background
		surface.globalCompositeOperation = "lighter";
		
		for(var i = 0; i < particles.length; i++)
		{
			var p = particles[i];
        
			surface.beginPath();

			p.opacity = Math.round(p.death/p.life*100)/100;
			var gradient = surface.createRadialGradient(p.location.x, p.location.y, 0, p.location.x, p.location.y, p.radius);
			gradient.addColorStop(0, "rgba("+p.r+", "+p.g+", "+p.b+", "+p.opacity+")");
			gradient.addColorStop(0.5, "rgba("+p.r+", "+p.g+", "+p.b+", "+p.opacity+")");
			gradient.addColorStop(1, "rgba("+p.r+", "+p.g+", "+p.b+", 0)");
			surface.fillStyle = gradient;
			surface.arc(p.location.x, p.location.y, p.radius, Math.PI*2, false);
			surface.fill();
			p.death--;
			p.radius++;
			p.location.x += (p.speed.x);
			p.location.y += (p.speed.y);
			
			//regenerate particles
			if(p.death < 0 || p.radius < 0){
				//a brand new particle replacing the dead one
				particles[i] = new particle();
			}
		}

		//requestAnimFrame(ParticleAnimation);
		timeout = requestAnimationFrame(ParticleAnimation);

	}

	//ParticleAnimation();
	timeout = requestAnimationFrame(ParticleAnimation);

	function firePos(w, h) {
		var x = w / 2,
			y = h - 15;

		var currX = getRendomNum(x - (x / 4 * 3), x + (x / 4 * 3));

		return {x: currX, y: y};
	}

	//rendom number
	function getRendomNum(min, max) {
		return Math.round(Math.random() * (max-min) + min);
	}

	function getAnimateFrame() {
		var lastTime = 0;
		var prefixes = 'webkit moz ms o'.split(' '); //各浏览器前缀

		var requestAnimationFrame = window.requestAnimationFrame;
		var cancelAnimationFrame = window.cancelAnimationFrame;

		var prefix;
		//通过遍历各浏览器前缀，来得到requestAnimationFrame和cancelAnimationFrame在当前浏览器的实现形式
		for( var i = 0; i < prefixes.length; i++ ) {
			if ( requestAnimationFrame && cancelAnimationFrame ) {
				break;
			}
			prefix = prefixes[i];
			requestAnimationFrame = requestAnimationFrame || window[ prefix + 'RequestAnimationFrame' ];
			cancelAnimationFrame  = cancelAnimationFrame  || window[ prefix + 'CancelAnimationFrame' ] || window[ prefix + 'CancelRequestAnimationFrame' ];
		}

		//如果当前浏览器不支持requestAnimationFrame和cancelAnimationFrame，则会退到setTimeout
		if ( !requestAnimationFrame || !cancelAnimationFrame ) {
			requestAnimationFrame = function( callback, element ) {
				var currTime = new Date().getTime();
				//为了使setTimteout的尽可能的接近每秒60帧的效果
				var timeToCall = Math.max( 0, 16 - ( currTime - lastTime ) );
				var id = window.setTimeout( function() {
					callback( currTime + timeToCall );
				}, timeToCall );
				lastTime = currTime + timeToCall;
				return id;
			};

			cancelAnimationFrame = function( id ) {
				window.clearTimeout( id );
			};
		}

		//得到兼容各浏览器的API
		window.requestAnimationFrame = requestAnimationFrame;
		window.cancelAnimationFrame = cancelAnimationFrame;
	}

}
