//
//  main.js
//
//  A project template for using arbor.js
//
(function($){


  var Renderer = function(canvas){
    var canvas = $(canvas).get(0);
    var ctx = canvas.getContext("2d");
    var particleSystem;
    window.addEventListener('resize', resizeCanvas, false);

    function resizeCanvas() {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;

      /**
       * Your drawings need to be inside this function otherwise they will be reset when
       * you resize the browser window and the canvas goes will be cleared.
       */
      drawStuff();
    }
    resizeCanvas();

    function drawStuff() {
      // do your drawing stuff here
    }

    var sphereObject = document.createElement('img');
    sphereObject.src = 'assets/img/img-project-sphere-selected.png';

    var background = document.createElement('img');
    background.src = 'assets/img/img-bg-1.jpg';

    var selectedObject = document.createElement('img');
    selectedObject.src = 'assets/img/img-project-sphere.png';


    var that = {
      init:function(system){
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height) 
        particleSystem.screenPadding(250) // leave an extra 80px of whitespace per side
        
        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },
      
      redraw:function(){
        // 
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        //

        /*ctx.fillStyle="#000";*/
        ctx.drawImage(background, 0, 0, canvas.width, canvas.height);
        
        particleSystem.eachEdge(function(edge, pt1, pt2){
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          // draw a line from pt1 to pt2
          if( edge.data.color == 'red' )
            ctx.strokeStyle = "rgba(255,0,0, .333)"
          else
            ctx.strokeStyle = "rgba(255,255,255, .333)"
          ctx.lineWidth = 1
          ctx.beginPath()
          ctx.moveTo(pt1.x, pt1.y)
          ctx.lineTo(pt2.x, pt2.y)
          ctx.stroke()
        })

        particleSystem.eachNode(function(node, pt){
          // node: {mass:#, p:{x,y}, name:"", data:{}}
          // pt:   {x:#, y:#}  node position in screen coords

          // draw a rectangle centered at pt
          var w = 25 + 25 * node.data.weight;
          ctx.shadowColor = (node.data.alone) ? "red" : "white";
          //ctx.fillRect(pt.x-w/2, pt.y-w/2, w,w)

          //console.log( sphereObject.getImageData(0,0,320,240) );

          ctx.save();

          ctx.shadowBlur = 0;
          if( node.data.selected ) {
            ctx.beginPath();
            ctx.strokeStyle = "#FFF"
            ctx.lineWidth = 2
            ctx.arc(pt.x, pt.y, w - 20, 0, Math.PI * 2, true);
            ctx.stroke();
            //ctx.fillStyle="#F00";
            //ctx.fill();
            ctx.closePath();
            ctx.shadowBlur = 0  ;
            ctx.drawImage(sphereObject, pt.x-w/2, pt.y-w/2, w, w);
          }
          else {
            ctx.drawImage(selectedObject, pt.x-w/2, pt.y-w/2, w, w);
          }

          ctx.restore();
        })    			
      },
      
      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          clicked:function(e){
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            dragged = particleSystem.nearest(_mouseP);

            if (dragged && dragged.node !== null){
              // while we're dragging, don't let physics move the node
              dragged.node.fixed = true

              particleSystem.eachNode(function(node, pt){
                node.data.selected = false;
              });

              dragged.node.data.selected = true;

            }

            $(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

            return false
          },
          dragged:function(e){
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (dragged && dragged.node !== null){
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            _mouseP = null
            return false
          },

          hover:function(e) {
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            dragged = particleSystem.nearest(_mouseP);

            if (dragged && dragged.node !== null && dragged.distance <= 10){

              $('#projectInfoPopup').text(dragged.node.data.title);
              $('#projectInfoPopup').show();
              $('#projectInfoPopup').css("top", e.pageY - pos.top - 50);
              $('#projectInfoPopup').css("left", e.pageX - pos.left + 20);

            }
          }
        }
        
        // start listening
        $(canvas).mousedown(handler.clicked);

        $(canvas).mousemove(handler.hover);

      },
      
    }
    return that
  }    

  $(document).ready(function(){
    var sys = arbor.ParticleSystem(1000, 600, 0.5, false, 30) // create the system with sensible repulsion/stiffness/friction
    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...

    // add some nodes to the graph and watch it go...
    sys.addNode('a', {weight: 3, title: "Epic"});
    sys.addNode('b', {weight: 2, title: "Story 1"});
    sys.addNode('c', {weight: 2, title: "Story 2"});

    //sys.addNode('d', {weight: 1, title: "Task 1"});
    //sys.addNode('e', {weight: 1, title: "Task 2"});
    //
    //sys.addNode('f', {weight: 1, title: "Task 3"});
    //sys.addNode('g', {weight: 1, title: "Task 4"});
    //sys.addNode('h', {alone:true, mass:.25, weight: 1, title: "Task 5"})

    sys.addEdge('a','b')
    sys.addEdge('a','c')
    //sys.addEdge('b','d')
    //sys.addEdge('b','e')
    //sys.addEdge('c','f')
    //sys.addEdge('c','g')

    //sys.addEdge('f','d', {color:'red'})

    // or, equivalently:
    //
    // sys.graft({
    //   nodes:{
    //     f:{alone:true, mass:.25}
    //   }, 
    //   edges:{
    //     a:{ b:{},
    //         c:{},
    //         d:{},
    //         e:{}
    //     }
    //   }
    // })
    
  })

})(this.jQuery)