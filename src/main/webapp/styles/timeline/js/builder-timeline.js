var Builder = function( scene, hlines ) {
    this.scene = scene;

    // grid vars
    this.hlines = hlines;
    this.laneInterval = 10;

    // objects vars
    this.materials = [];
    this.animations = [];
    this.vlanes = [];
    this.hlanes = [];
    this.lanesName = [];
    this.dates = [];


    // world controlling vars
    this.camera;
    this.cameraZ = -50;
    this.world;
    this.subPlane;

    this.zPos = 0;
    this.zPosDate = -100000;
    this.lastDate = new Date("2015-07-21"); // @todo
    this.activeDate = new Date("2015-07-21");

    this.LEFT_CAM_LIMIT = 0;
    this.RIGHT_CAM_LIMIT = 0;
};

Builder.prototype.createBaseScene = function() {
    this.camera = new BABYLON.FreeCamera("camera", new BABYLON.Vector3(0, 50, this.cameraZ), this.scene);
    this.camera.setTarget(new BABYLON.Vector3(0, 0, 90));
    this.camera.attachControl(canvas, false);

    new BABYLON.DirectionalLight("lightF", new BABYLON.Vector3(0, -1, 1), this.scene);
    new BABYLON.DirectionalLight("lightB", new BABYLON.Vector3(0, 1, 1), this.scene);

    this.scene.ambientColor = new BABYLON.Color3(1, 1, 1);

    var background = new BABYLON.Mesh.CreatePlane( "background", 280, scene );
    background.position.z = 110;
    background.position.y = -50;
    background.material = this.materials['background'];
    background.rotation.x = .35;
    this.setRelatedToCamPos( background );


    var overlay = new BABYLON.Mesh.CreatePlane( "overlay", 280, scene );
    overlay.position.z = 95;
    overlay.position.y = -45;
    //overlay.material.alphaMode = BABYLON.Engine.ALPHA_ADD;
    overlay.material = this.materials['overlay'];
    overlay.rotation.x = 0.35; // @todo add calculations
    this.setRelatedToCamPos( overlay );

    overlay.registerBeforeRender(function() {
        engine.setAlphaMode(BABYLON.Engine.ALPHA_ADD);
    });

    var keys = [{frame:0,  value: 0}, {frame:100,value: 1}];
    var animation = new BABYLON.Animation('background-anim', 'rotation.z', 5, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    animation.setKeys(keys);
    overlay.animations.push(animation);
    this.scene.beginAnimation(overlay, 0, 100, true);


    this.subPlane = new BABYLON.Mesh.CreatePlane("subPlane", 1, this.scene);
    this.subPlane.scaling.y = (this.hlines - 1) * this.laneInterval + Math.floor( Math.abs( this.cameraZ ) / 2 );
    this.subPlane.rotation.x = Math.PI / 2;
    //this.subPlane.position.z = this.subPlane.scaling.y / 2;
    this.subPlane.position.y = -5;
    this.subPlane.material = this.materials[ 'subPlane' ];

    this.setRelatedToCamPos( this.subPlane );

    this.subPlane.registerBeforeRender(function() {
        engine.setAlphaMode(BABYLON.Engine.ALPHA_COMBINE);
    });
};


Builder.prototype.updateGrid = function( laneNum ) {

    for( var i = 0; i < this.hlanes.length; i++ ) {
        this.hlanes[i].dispose();
    }
    this.hlanes = [];

    for( var i = 0; i < this.vlanes.length; i++ ) {
        this.vlanes[i].dispose();
    }
    this.vlanes = [];

    this.subPlane.scaling.x = this.laneInterval * laneNum;
    this.subPlane.position.x = this.laneInterval * laneNum / 2;

    this.fadeIn( this.subPlane );

    for( var i = 0; i < this.hlines + 2; i++ ) {
        this.createHLane(i, i, laneNum);
    }

    for( var i = 0; i < laneNum + 1; i++ ) {
        this.createVLane(i - this.laneInterval, i, 0);
    }

    this.camera.position.x = this.LEFT_CAM_LIMIT;

    this.RIGHT_CAM_LIMIT = this.laneInterval * (laneNum - 1);
};


// dates pos

Builder.prototype.insertDates = function (pos) {
    if( pos > this.zPosDate ) {
        this.zPosDate = pos;

        this.addDates(pos * this.laneInterval, this.lastDate.format("mmm dd, yyyy"));
        this.lastDate.setDate( this.lastDate.getDate() + 1 );
    }
};

Builder.prototype.addDates = function (posZ, text) {

    var planeTexture = new BABYLON.DynamicTexture("dynamic texture", 1024, this.scene, true);
    planeTexture.hasAlpha = true;

    var planeMaterial = new BABYLON.StandardMaterial("plane material", this.scene);
    planeMaterial.backFaceCulling = false;
    planeMaterial.specularColor = new BABYLON.Color3(0, 0, 0);

    planeMaterial.diffuseTexture = planeTexture;


    var plane = BABYLON.Mesh.CreatePlane("id_" + text, 4, this.scene);
    plane.material = planeMaterial;
    plane.rotation.x = Math.PI / 5;

    planeTexture.drawText(text, null, 150, "bold 80px GothamPro", "white", null);

    plane.scaling = new BABYLON.Vector3(1, 1, 0.05);
    plane.position = new BABYLON.Vector3(-this.hLineOffset / 2, -1, posZ);

    if( this.dates.length > 0 ) {
        plane.position.x = this.dates[0].position.x;
    }

    this.dates.push( plane );
};

// axillary functions

Builder.prototype.setRelatedToCamPos = function( mesh ) {
    mesh.position.z -= this.cameraZ;
    mesh.infiniteDistance = true;
};

Builder.prototype.fadeIn = function(obj){

    var keys = [];
    keys.push({frame:0, value:0});
    keys.push({frame:50,value:1});

    var alpha = new BABYLON.Animation('alpha', 'alpha', 50, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    alpha.setKeys(keys);

    keys = [];
    keys.push({frame:0, value:-3});
    keys.push({frame:50,value:0});

    var yMvmnt = new BABYLON.Animation('yMvmnt', 'position.y', 50, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    yMvmnt.setKeys(keys);

    obj.animations.push(alpha);
    obj.animations.push(yMvmnt);

    scene.beginAnimation(obj, 0, 50, false);
};

Builder.prototype.createVLane = function (id, xPos, zPos) {

    var lane = BABYLON.Mesh.CreateLines("vline" + id, [
        new BABYLON.Vector3(0, 0, 0),
        new BABYLON.Vector3(0, 0, this.laneInterval * (this.hlines + 1))
    ], this.scene);

    lane.position.x = xPos * this.laneInterval;
    lane.position.z = zPos;

    lane.color = this.materials[ 'gridcolor' ];

    this.vlanes.push(lane);

    this.fadeIn( lane );

    this.buildArc( xPos, 3 * this.laneInterval );
};


Builder.prototype.createHLane = function (id, xPos, width) {
    var lane = BABYLON.Mesh.CreateLines("hline" + id, [
        new BABYLON.Vector3(0, 0, 0),
        new BABYLON.Vector3(this.laneInterval, 0, 0)
    ], this.scene);

    lane.scaling.x = width;
    lane.position.z = xPos * this.laneInterval;

    lane.color = this.materials[ 'gridcolor' ];

    this.hlanes.push( lane );

    this.fadeIn( lane );
};


Builder.prototype.buildArc = function(startPoint, width, dashed){
    var arcPoints = BABYLON.Curve3.CreateQuadraticBezier(
        startPoint,
        new BABYLON.Vector3(width / 2, width *.4, 0),
        new BABYLON.Vector3(width, 0, 0),
        100);

    if (!dashed){
        var curve = BABYLON.Mesh.CreateLines("curve", arcPoints.getPoints(), scene);
        curve.color = new BABYLON.Color3(0, 1, 1);
    } else{
        var curve2 = BABYLON.Mesh.CreateDashedLines('arc', arcPoints.getPoints(), 200, 200, 200, this.scene);
        curve2.color = new BABYLON.Color3(0, 1, 1);
    }
};

Builder.prototype.createIssue = function (id, name, xPos, zPos, length, type) {
    xPos += this.laneInterval;
    var lane = BABYLON.Mesh.CreateCylinder("issue" + id, length, 0.3, 0.3, 8, this.scene, false);
    lane.rotation.x = Math.PI / 2;
    lane.position.x = xPos;
    lane.position.z = length / 2 + zPos;
    lane.material = this.materials[ type ];

    this.fadeIn( lane );


    if( scene.getMeshByID("issueName" + id) == null ) {
        var issueName = BABYLON.Mesh.CreatePlane("issueName" + id, 20, scene, false);
        issueName.material = new BABYLON.StandardMaterial("issueName" + id, scene);

        //background.lookAt(b.camera.position);
        issueName.rotation.x = Math.PI / 2;
        issueName.rotation.z = Math.PI / 2;
        issueName.position.x = 4 + xPos;
        issueName.position.z = this.zPos + 20;
        issueName.position.y = 0.1;

        var issueNameTexture = new BABYLON.DynamicTexture("dynamic texture", 512, scene, true);
        issueNameTexture.hasAlpha = true;
        issueName.material.diffuseTexture = issueNameTexture;
        issueName.material.diffuseColor = new BABYLON.Color3(2.55, 2.55, 2.55);
        issueName.material.specularColor = new BABYLON.Color3(0, 0, 0);
        issueName.material.backFaceCulling = false;

        issueNameTexture.drawText(name, null, 80, "bold 70px GothamPro", "white", null);

        this.lanesName.push(issueName);
    }
};

Builder.prototype.createEvent = function (id, pId, position, timePoint, eventType, issueType, text) {

    var material;
    var radius = 0.25

    switch (eventType) {
        case "JIRA" :
            material = this.materials[ issueType ];
            radius = 0.4;
            break;
        case "Stash" :
            material = this.materials[ "research" ];
            break;
        case "Commit" :
            material = this.materials[ "research" ];
            break;
        case "Block" :
            material = this.materials[ "bug" ];
            break;
    }
    // todo

    var object = BABYLON.Mesh.CreatePlane( "event" + id, 1.5, this.scene );
    object.position.x = position;
    object.position.z = timePoint;



    var workLogM = new BABYLON.StandardMaterial("workLogM", scene);
    workLogM.diffuseTexture = new BABYLON.Texture("../styles/timeline/assets/img/Icon2.svg", scene);
    workLogM.diffuseTexture.hasAlpha = true;
    workLogM.diffuseTexture.uScale = 1;
    workLogM.diffuseTexture.vScale = 1;
    workLogM.opacityTexture = workLogM.diffuseTexture;

    workLogM.specularColor = new BABYLON.Color3( 0, 0, 0 );
    workLogM.alpha = 0.5;



    object.material = workLogM;

    this.addPopup(pId, id, object, text);
};

/*
    Function to add popup with the specified text
 */
Builder.prototype.addPopup = function (id, eventid, mesh, text) {
    mesh.actionManager = new BABYLON.ActionManager(this.scene);
    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPointerOverTrigger, function(){ popup( mesh, text ) }));
    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPointerOutTrigger, function(){ popupHide( mesh ) }));

    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPickTrigger, function(){ showMinimap( id, eventid ) }));
};


/*
    Materials initialization
    materials[ key ] array
 */

Builder.prototype.createMaterials = function() {
    var scene = this.scene;

    var backgroundTexture = new BABYLON.Texture("../styles/timeline/assets/skybox/BACKGR_SQUARE.png", scene);

    var background = new BABYLON.StandardMaterial("background", scene);
    background.specularColor = new BABYLON.Color3(0, 0, 0);
    background.ambientTexture = backgroundTexture;


    var overlayTexture = new BABYLON.Texture("../styles/timeline/assets/skybox/skybox_nx.jpg", scene);

    var overlay = new BABYLON.StandardMaterial("overlay", scene);
    overlay.ambientTexture = overlayTexture;
    overlay.specularColor = new BABYLON.Color3(0, 0, 0);
    overlay.alpha = 0.2;


    this.materials[ 'gridcolor' ] = new BABYLON.Color3(0.5, 0.5, 0.5);
    this.materials[ 'subPlane' ] = this.createMaterialFromRGB( "subPlane", scene, 0, 0.3, 1.0, 0.2 );
    this.materials[ 'background' ] = background;
    this.materials[ 'overlay' ] = overlay;

    //this.materials[ 'Task' ] = this.createMaterialFromRGB( "task", scene, .2, 2.19, 2.34 );
    this.materials[ 'Task' ] = this.createMaterialFromRGB( "task", scene, 2.55, 2.55, .51 );
    this.materials[ 'Research' ] = this.createMaterialFromRGB( "research", scene, 0, 2.55, 1.02 );
    this.materials[ 'Bug' ] = this.createMaterialFromRGB( "bug", scene, 2.34, 0.2, 0.5 );
    this.materials[ 'Requirement' ] = this.createMaterialFromRGB( "requirement", scene, 2.34, 0.2, 0.5 );
    this.materials[ 'Design' ] = this.createMaterialFromRGB( "design", scene, 2.34, 0.2, 0.5 );
    this.materials[ 'Playbook' ] = this.createMaterialFromRGB( "playbook", scene, 2.34, 0.2, 0.5 );
};

Builder.prototype.createMaterialFromRGB = function( name, scene, r, g, b, a ) {
    if( a == undefined || a == null ) {
        a = 1;
    }

    var material = new BABYLON.StandardMaterial( name , scene );
    material.specularColor = new BABYLON.Color3(0, 0, 0);
    material.diffuseColor = new BABYLON.Color3( r, g, b);
    material.alpha = a;

    return material;
};


Builder.prototype.responseToCameraEvents = function( camSpeed )
{
    if( this.camera.position.x + camSpeed.x >= this.LEFT_CAM_LIMIT && this.camera.position.x + camSpeed.x <= this.RIGHT_CAM_LIMIT ) {
        this.camera.position.x += camSpeed.x;

        for( var i = 0; i < this.dates.length; i++ ) {
            this.dates[i].position.x += camSpeed.x;
        }
    }

    //@ todo z max limit
    if( this.camera.position.z + camSpeed.z >= this.cameraZ && this.camera.position.z + camSpeed.z <= 10000 ) {
        this.camera.position.z += camSpeed.z;
        for( var i = 0; i < this.lanesName.length; i++ ) {
            this.lanesName[i].position.z += camSpeed.z;
        }

        for( var i = 0; i < this.vlanes.length; i++ ) {
            this.vlanes[i].position.z += camSpeed.z;
        }

        var interval = parseInt(( this.camera.position.z - this.cameraZ ) / this.laneInterval );
        //if( this.zPos < interval ) {
        //    for( var i = zPos; i <= interval; i++ ) {
        //
        //    }
        //}
    }
}


Builder.prototype.launchParticles = function() {
    var point = BABYLON.Mesh.CreateSphere('point',.01, .01, scene);
    point.position.x = 50;
    point.position.z = 100;

    var particleSystem = new BABYLON.ParticleSystem("particles", 2000, this.scene);
    particleSystem.particleTexture = new BABYLON.Texture("../styles/timeline/assets/flare.png", this.scene);

    // Where the particles come from
    particleSystem.emitter = point; // the starting object, the emitter
    particleSystem.minEmitBox = new BABYLON.Vector3(0, 0, 0); // Starting all from
    particleSystem.maxEmitBox = new BABYLON.Vector3(1, 1, 1); // To...

    // Colors of all particles
    particleSystem.color1 = new BABYLON.Color4(0.7, 0.8, 1.0, 1.0);
    particleSystem.color2 = new BABYLON.Color4(0.2, 0.5, 1.0, 1.0);
    particleSystem.colorDead = new BABYLON.Color4(0, 0, 0.2, 0.0);

    // Size of each particle (random between...
    particleSystem.minSize = .1;
    particleSystem.maxSize = .5;

    // Life time of each particle (random between...
    particleSystem.minLifeTime = 1;
    particleSystem.maxLifeTime = 5;

    // Emission rate
    particleSystem.emitRate = 100;

    // Blend mode : BLENDMODE_ONEONE, or BLENDMODE_STANDARD
    particleSystem.blendMode = BABYLON.ParticleSystem.BLENDMODE_ONEONE;

    // Set the gravity of all particles
    particleSystem.gravity = new BABYLON.Vector3(0, 10, -10);

    // Direction of each particle after it has been emitted
    particleSystem.direction1 = new BABYLON.Vector3(-100, 50, 50);
    particleSystem.direction2 = new BABYLON.Vector3(100,  50, -500);

    // Angular speed, in radians
    particleSystem.minAngularSpeed = 1;
    particleSystem.maxAngularSpeed = Math.PI;

    // Speed
    particleSystem.minEmitPower = 1;
    particleSystem.maxEmitPower = 3;
    particleSystem.updateSpeed = 0.001;

    particleSystem.start();
};

function popup( mesh, text ) {
    if( !MOVE_CAMERA ) {
        mesh.material.alpha = 1;
        $('#popup').text( text );
        $('#popup').show();
        $('#popup').css("top", ( window.event.clientY - 110 ) + "px");
        $('#popup').css("left", (window.event.clientX - 125) + "px");
        doMouseUp();
    }
}

function popupHide(mesh) {
    mesh.material.alpha = 0.5;
    $('#popup').hide();
}

var SCREEN_WIDTH = window.innerWidth;
var SCREEN_HEIGTH = window.innerHeight;
var Z_INDEX = 10000;

function showMinimap( id, eventId ) {


    doMouseUp();
    var strBuilder;

    var utmost = false;
    var popupLimit = 5;

    $.grep(DATA, function(e){ return e.id == id; });

    for( var i = DATA[id].events.length - 1; i >= 0  ; i-- ) {
        if( DATA[id].events[i].id == eventId ) {
            strBuilder = '<div style="width: ' + SCREEN_WIDTH / 6 * 4 + 'px; height: ' + SCREEN_HEIGTH / 6 * 4 + 'px;  left: ' + SCREEN_WIDTH / 6 + 'px; top: ' + SCREEN_HEIGTH / 6 + 'px; z-index: ' + Z_INDEX-- + ';" class="document-lister">' + DATA[id].events[i].eventInfo + '<a style="display: block;">Close</a></div>';

            $('body').append( strBuilder );
            utmost = true;
        }
        else if( utmost && popupLimit-- >= 0 ) {
            strBuilder = '<div style="width: ' + (SCREEN_WIDTH / 6 * 4 - ( 5 - popupLimit ) * 10) + 'px; height: ' + SCREEN_HEIGTH / 6 * 4 + 'px;  left: ' + (SCREEN_WIDTH / 6 + ( 5 - popupLimit ) * 5) + 'px; top: ' + ( SCREEN_HEIGTH / 6 - ( 5 - popupLimit ) * 20 ) + 'px; z-index: ' + Z_INDEX-- + ';" class="document-lister">' + DATA[id].events[i].eventInfo + '</div>';

            $('body').append( strBuilder );
        }
    }


    //for( var i = 0; i <  DATA[id].events.length; i++ ) {
    //    strBuilder += '<div class="minimap-event" pid="' + DATA[id].events[i].id + '"><div class="minimap-event-date">' + DATA[id].events[i].eventDate + '</div><div class="minimap-event-descr">' + DATA[id].events[i].eventInfo + '</div></div>';
    //}
    //
    //$('#minimap .minimap-info').attr('pid', id);
    //
    //$('#minimap .minimap-info').html(strBuilder);
    //
    //$('#minimap .minimap-event[pid="' + eventId + '"]').css("color", "yellow");
    //$('#minimap').show();
    //
    //
    //$('#minimap .minimap-info').animate( { scrollTop: $('#minimap .minimap-info').scrollTop() + $('#minimap .minimap-event[pid="' + eventId + '"]').position().top } );
    //this.scene.getMeshByID("event"+eventId).material.alpha = 1;
}

var MOVE_CAMERA = true;

function doMouseUp(e) {
    MOVE_CAMERA = false;
};