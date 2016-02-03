var Builder = function (engine, startDate, currentDate, getDataFunction) {
    this.engine = engine;

    this.getDataFunction = getDataFunction;

    // grid vars
    this.hlines = 10;
    this.vlaneInterval = 10;
    this.hlaneInterval = 7;
    this.intervalPreloadPos = 5;
    this.correctRotation = .33;

    // objects vars
    this.materials = [];
    this.meshes = [];
    this.textures = [];

    this.vlanes = [];
    this.hlanes = [];
    this.lanesName = [];
    this.angleFactorWaveEffect = 2;


    // world controlling vars
    this.camera;
    this.world;
    this.subPlane;
    this.zeroPointMovableObjs;

	//vertical Line Parents Points
	this.currentPoint;
	this.nextPoint;
	this.prevPoint;

	//dates parent points
	this.dateCurrentPoint;
	this.dateNextPoint;
	this.datePrevPoint;	

    this.CAMERA_POS = new BABYLON.Vector3(0, 50, -50);
    this.CAMERA_TARGET = new BABYLON.Vector3(0, 0, this.CAMERA_POS.z + 140);

    this.zPos = 0;
    this.startDate = startDate;
    this.activeDate = currentDate;

    this.LEFT_CAM_LIMIT = 0;
    this.RIGHT_CAM_LIMIT = 0;
    this.LANES_NUM = 0;

    // @todo formatting lib to remove this
	this.monthNames = [
		"Jan", "Feb", "Mar",
		"Apr", "May", "June", "July",
		"Aug", "Sept", "Oct",
		"Nov", "Dec"
	];

    this.scene = new BABYLON.Scene(engine);

    this.audioClick = new Audio('assets/audio/Click.wav');
    this.audioOpen = new Audio('assets/audio/openSound.wav');
};

/*********************************
 init functions
 */

/**
 * meshes @todo refactor
 * @param texture
 * @param x
 * @param z
 */
Builder.prototype.loadMeshes = function (texture, x, z) {
    BABYLON.SceneLoader.ImportMesh("", "assets/", "mesh.babylon", this.scene, function (meshes) {

        //meshes[0].position.x = x;
        //meshes[0].position.z = z;
        //meshes[0].rotation.x = -Math.PI / 2;
        //
        //meshes[1].position.x = x;
        //meshes[1].position.z = z;
        //meshes[1].rotation.x = -Math.PI / 2;
        //
        //meshes[2].position.x = x;
        //meshes[2].position.z = z;
        //meshes[2].rotation.x   = -Math.PI / 2;
        //
        //meshes[3].position.x = x;
        //meshes[3].position.z = z;
        //meshes[3].rotation.x = -Math.PI / 2;
        //
        //meshes[4].position.x = x;
        //meshes[4].position.z = z;
        //meshes[4].rotation.x = -Math.PI / 2;
        //
        //meshes[5].position.x = x;
        //meshes[5].position.z = z;
        //meshes[5].rotation.x = -Math.PI / 2;
        //
        //
        //var material = new BABYLON.StandardMaterial("texture", this.scene);
        //material.specularColor = new BABYLON.Color3.Black;
        //material.diffuseTexture = new BABYLON.Texture(texture, this.scene);
        //material.diffuseTexture.hasAlpha = true;
        //meshes[5].material = material;
        //
        //
        //for (var i = 0; i < meshes.length; ++i) {
        //    if (i == 0) {
        //        setAnimation(meshes[i], -0.5, -0.5, 0);//3
        //    }
        //    else if (i == 1) {
        //        setAnimation(meshes[i], 0.5, -0.5, 0);//4
        //    }
        //    else if (i == 2) {
        //        setAnimation(meshes[i], 0.5, 0.5, 0);//2
        //    }
        //    else if (i == 3) {
        //        setAnimation(meshes[i], -0.5, 0.5, 0);//1
        //    }
        //    else if (i == 4) {
        //        var animation;
        //        var keys = [];
        //        animation = new BABYLON.Animation("Animation", "rotation", 30,
        //            BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_RELATIVE);
        //        var Rotation = new BABYLON.Vector3(-Math.PI / 2, 0, 0);
        //        var nextRotation = new BABYLON.Vector3(-Math.PI / 2, 0, -Math.PI / 2);
        //        keys.push({frame: 0, value: Rotation});
        //        keys.push({frame: 36, value: nextRotation});
        //        animation.setKeys(keys);
        //        meshes[4].animations.push(animation);
        //
        //
        //        this.scene.beginAnimation(meshes[4], 0, 36, true, 1);
        //    }
        //    else if (i == 5) {
        //        setAnimation(meshes[i], 0, 0, -0.5);
        //    }
        //}
        //
        //prepareButton(meshes);
    });

    var setAnimation = function (mesh, x, y, z) {
        var animation = new BABYLON.Animation("Animation", "position", 30,
            BABYLON.Animation.ANIMATIONTYPE_VECTOR3, BABYLON.Animation.ANIMATIONLOOPMODE_CONSTANT);
        var nextPos = mesh.position.add(new BABYLON.Vector3(x, y, z));
        var keys = [];
        keys.push({frame: 0, value: mesh.position});
        keys.push({frame: 36, value: nextPos});
        keys.push({frame: 72, value: mesh.position});
        animation.setKeys(keys);
        mesh.animations.push(animation);

    };

    var prepareButton = function (mesh) {
        var func = function () {
            this.scene.beginAnimation(mesh[0], 0, 36, true);
            this.scene.beginAnimation(mesh[1], 0, 36, true);
            this.scene.beginAnimation(mesh[2], 0, 36, true);
            this.scene.beginAnimation(mesh[3], 0, 36, true);

            this.scene.beginAnimation(mesh[4], 0, 36, true, 0);//vnewniy krug
            this.scene.beginAnimation(mesh[5], 0, 36, true);
        };

        var func_d = function () {

            this.scene.beginAnimation(mesh[0], 36, 72, true);
            this.scene.beginAnimation(mesh[1], 36, 72, true);
            this.scene.beginAnimation(mesh[2], 36, 72, true);
            this.scene.beginAnimation(mesh[3], 36, 72, true);

            this.scene.beginAnimation(mesh[4], 0, 36, true, 1);
            this.scene.beginAnimation(mesh[5], 36, 72, true);
        };

        mesh[5].actionManager = new BABYLON.ActionManager(this.scene);
        mesh[5].actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPickTrigger, func))
            .then(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPickTrigger, func_d));
    };

};

/**
 * materials
 */
Builder.prototype.createMaterials = function () {
    var scene = this.scene;

    var backgroundTexture = new BABYLON.Texture("assets/skybox/BACKGR_SQUARE.png", scene);

    var background = new BABYLON.StandardMaterial("background", scene);
    background.specularColor = new BABYLON.Color3(0, 0, 0);
    background.ambientTexture = backgroundTexture;


    var overlayTexture = new BABYLON.Texture("assets/skybox/skybox_nx.jpg", scene);

    var overlay = new BABYLON.StandardMaterial("overlay", scene);
    overlay.ambientTexture = overlayTexture;
    overlay.specularColor = new BABYLON.Color3(0, 0, 0);
    overlay.alpha = 0.2;


    var datesBackgroundT = new BABYLON.Texture("assets/img/textBackground.png", scene);
    datesBackgroundT.hasAlpha = true;
    datesBackgroundT.opacityTexture = datesBackgroundT.diffuseTexture;

    var datesBackground = new BABYLON.StandardMaterial("datesBackground", scene);
    datesBackground.diffuseTexture = datesBackgroundT;
    datesBackground.opacityTexture = datesBackgroundT;
    datesBackground.specularColor = new BABYLON.Color3(0, 0, 0);
    //overlay.alpha = 0.2;



    this.materials['gridcolor'] = new BABYLON.Color3(0.2, 0.2, 0.2);
    this.materials['child'] = new BABYLON.Color3(2.55, 2.04, 0);
    this.materials['block'] = new BABYLON.Color3(2.55, 0.2, 0.2);

    this.materials['subPlane'] = this.createMaterialFromRGB("subPlane", scene, 0, 0.3, 1.0, 0.2);
    this.materials['background'] = background;
    this.materials['overlay'] = overlay;
    this.materials['datesBackground'] = datesBackground;

    // issue types
    this.materials['task'] = this.createMaterialFromRGB("task", scene, .51, 1.53, 2.55);
    this.materials['research'] = this.createMaterialFromRGB("research", scene, 0, 1.53, .51);
    this.materials['bug'] = this.createMaterialFromRGB("bug", scene, 2.04, 0,.51);
    this.materials['requirement'] = this.createMaterialFromRGB("requirement", scene, 2.34, 0.2, 0.5);
    this.materials['design'] = this.createMaterialFromRGB("design", scene, 2.55, 2.04, 0);
    this.materials['playbook'] = this.createMaterialFromRGB("playbook", scene, 2.04, 2.04, 2.04);

    this.materials['tranparent'] = this.createMaterialFromRGB("transparent", scene, 0, 0, 0, 0);

};

/**
 * Wrapper to create material from rgba
 * @param name - id
 * @param scene
 * @param r - red
 * @param g - green
 * @param b - blue
 * @param a - material.alpha
 * @returns {*|StandardMaterial}
 */
Builder.prototype.createMaterialFromRGB = function (name, scene, r, g, b, a) {
    if (a == undefined || a == null) {
        a = 1;
    }

    var material = new BABYLON.StandardMaterial(name, scene);
    material.specularColor = new BABYLON.Color3(0, 0, 0);
    material.diffuseColor = new BABYLON.Color3(r, g, b);
    material.alpha = a;

    return material;
};

/**
 * Create scene
 */
Builder.prototype.createBaseScene = function () {
    this.camera = new BABYLON.FreeCamera("camera", this.CAMERA_POS, this.scene);
    this.camera.setTarget(this.CAMERA_TARGET);

    new BABYLON.DirectionalLight("lightF", new BABYLON.Vector3(0, -1, 1), this.scene);
    new BABYLON.DirectionalLight("lightB", new BABYLON.Vector3(0, 1, 1), this.scene);

    //this.scene.ambientColor = new BABYLON.Color3(1, 1, 1);

    var background = BABYLON.Mesh.CreatePlane("background", 280, this.scene);
    background.position.z = ( this.hlines + 1 ) * 10;
    background.position.y = -50;
    background.material = this.materials['background'];
    background.rotation.x = this.correctRotation;
    this.setRelatedToCamPos(background);

    //@todo replace?
    //var overlay = BABYLON.Mesh.CreatePlane("overlay", 280, this.scene);
    //overlay.position.z = this.hlines * 10 - 5;
    //overlay.position.y = -45;
    //overlay.material = this.materials['overlay'];
    //overlay.rotation.x = this.correctRotation;
    //this.setRelatedToCamPos(overlay);
    //
    //overlay.registerBeforeRender(function () {
    //    this.engine.setAlphaMode(BABYLON.Engine.ALPHA_ADD);
    //});
    //
    //var keys = [{frame: 0, value: 0}, {frame: 100, value: 1}];
    //var animation = new BABYLON.Animation('background-anim', 'rotation.z', 5, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    //animation.setKeys(keys);
    //overlay.animations.push(animation);
    //this.scene.beginAnimation(overlay, 0, 100, true);

    this.zeroPointMovableObjs = this.createParentPointParent(0);

    this.subPlane = BABYLON.Mesh.CreatePlane("subPlane", 1, this.scene);
    this.subPlane.parent = this.zeroPointMovableObjs;
    this.subPlane.scaling.y = this.hlines * this.vlaneInterval;
    this.subPlane.rotation.x = Math.PI / 2;
    this.subPlane.position.z = this.subPlane.scaling.y / 2;
    this.subPlane.position.y = -1;
    this.subPlane.material = this.materials['subPlane'];

    var self = this;
    this.subPlane.registerBeforeRender(function () {
        self.engine.setAlphaMode(BABYLON.Engine.ALPHA_COMBINE);
    });
};

/*********************************
 basic build functions
 */

/**
 * Parent point (plane) - transperent object to use relative pos for many objects
 * @param posZ
 * @param posX - could be null = 0
 * @returns {plane}
 */
Builder.prototype.createParentPointParent = function (posZ, posX) {
	posX = typeof a !== 'undefined' ? posX * this.hlaneInterval : 0;
    var parentPoint = BABYLON.Mesh.CreatePlane("zeroPointMovableObjs_" + posZ, 1, this.scene);

    parentPoint.position.z = posZ;
	parentPoint.position.x = posX;
    parentPoint.material = this.materials['tranparent'];

	return parentPoint;
};

/**
 * create vertical line for grid
 * @param id
 * @param xPos
 * @param zPos
 */
Builder.prototype.createVLane = function (id, xPos, zPos) {

    var lane = BABYLON.Mesh.CreateLines("vline_" + id, [
        new BABYLON.Vector3(0, 0, 0),
        new BABYLON.Vector3(0, 0, this.vlaneInterval * this.hlines)
    ], this.scene);

    lane.position.x = xPos * this.hlaneInterval;
    lane.position.z = zPos;

    lane.parent = this.zeroPointMovableObjs;

    lane.color = this.materials['gridcolor'];

    this.vlanes.push(lane);

    this.fadeIn(lane);
};

/**
 * create horisontal lines using on hlines var
 * @param width - build always from 0
 * @param parentObj
 */
Builder.prototype.createHLanes = function (width, parentObj) {
    for (var i = 0; i < this.hlines; i++) {
		var lane = BABYLON.Mesh.CreateLines("hline_" + parentObj.position.z + '_' + i, [
			new BABYLON.Vector3(0, 0, 0),
			new BABYLON.Vector3(this.hlaneInterval, 0, 0)
		], this.scene);

		lane.scaling.x = width;
		lane.position.z = i * this.vlaneInterval;

		lane.parent = parentObj;

		lane.color = this.materials['gridcolor'];

        this.fadeIn(lane);
		this.hlanes.push(lane);
    }	
};


/**
 * create connecting Arc
 * @param xPos
 * @param zPos
 * @param width
 * @param type
 */
Builder.prototype.createArc = function(xPos, zPos, width, type) {
    var arcPoints = BABYLON.Curve3.CreateQuadraticBezier(
        new BABYLON.Vector3( xPos * this.hlaneInterval, 0, zPos ),
        new BABYLON.Vector3( xPos * this.hlaneInterval + width * this.hlaneInterval / 2, Math.abs( width ) * this.hlaneInterval * .5, zPos ),
        new BABYLON.Vector3( xPos * this.hlaneInterval + width * this.hlaneInterval, 0, zPos ),
        100
    );

    var curve = BABYLON.Mesh.CreateLines('curve', arcPoints.getPoints(), this.scene);
    //var curve = BABYLON.Mesh.CreateDashedLines('arc', arcPoints.getPoints(), 10, 10, 200, this.scene);

    curve.color = this.materials[ type ];

    this.fadeIn(curve);
};

/**
 * create issue lane
 * @param id
 * @param name
 * @param xPos
 * @param zPos
 * @param length
 * @param type
 */
Builder.prototype.createIssue = function (id, name, xPos, zPos, length, type, popup) {
    var lane = BABYLON.Mesh.CreateCylinder("issue_" + id, length, 0.3, 0.3, 8, this.scene, false);
    lane.rotation.x = Math.PI / 2;
    lane.position.x = xPos  * this.hlaneInterval;
    lane.position.z = length / 2 + zPos;

    lane.material = this.materials[type.toLowerCase()];

    lane.actionManager = new BABYLON.ActionManager(this.scene);
    lane.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPickTrigger, function () {
        popup.getIssuePopup(id);
    }));

    if (this.scene.getMeshByID(name) == null) {
        var issueName = BABYLON.Mesh.CreatePlane(name, 10, this.scene, false);
        issueName.material = new BABYLON.StandardMaterial("issueName_" + id, this.scene);

        issueName.rotation.x = Math.PI / 2;
        issueName.rotation.z = Math.PI / 2;
        issueName.position.x = xPos  * this.hlaneInterval;
        issueName.position.z = this.zPos + 20;
        issueName.position.y = 0.1;


        var issueNameTexture = new BABYLON.DynamicTexture("dynamic texture", 512, this.scene, true);
        issueNameTexture.hasAlpha = true;
        issueName.material.diffuseTexture = issueNameTexture;
        issueName.material.diffuseColor = new BABYLON.Color3(2.55, 2.55, 2.55);
        issueName.material.specularColor = new BABYLON.Color3(0, 0, 0);
        issueName.material.backFaceCulling = false;
        issueName.parent = this.zeroPointMovableObjs;

        issueNameTexture.drawText(name, null, 150, "bold 70px GothamPro", "white", null);

        this.fadeIn(lane);
        this.lanesName.push(issueName);
    }
};

/**
 * create event on issue lane
 * @param id
 * @param pId
 * @param position
 * @param timePoint
 * @param eventType
 * @param issueType
 * @param text
 */
Builder.prototype.createEvent = function (id, pId, position, timePoint, eventType, issueType, popup) {

	var radius = 5;

	var object = BABYLON.Mesh.CreatePlane( "event" + id, radius, this.scene );
	object.position.x = position;
	object.position.z = timePoint;

	var workLogM = new BABYLON.StandardMaterial("workLogM", this.scene);
	workLogM.diffuseTexture = new BABYLON.Texture("assets/img/Icon2.svg", this.scene);
	workLogM.diffuseTexture.hasAlpha = true;
	workLogM.diffuseTexture.uScale = 1;
	workLogM.diffuseTexture.vScale = 1;
	workLogM.opacityTexture = workLogM.diffuseTexture;

	workLogM.specularColor = new BABYLON.Color3( 0, 0, 0 );
	workLogM.alpha = 0.5;

	object.material = workLogM;

	//this.loadMeshes("assets/work.png", position, timePoint);

	this.addPopup(pId, id, object, popup);
};

/**
 * create dates related to the grid
 * @param stardDate
 * @param parentObj
 */
Builder.prototype.createDate = function (stardDate, parentObj) {

	var dateArray = this.getDatesBetween(stardDate, stardDate.addDays(this.hlines - 1));
	for (var i = 0; i < dateArray.length; i++) {
		var text = dateArray[i].getDate() + ' ' + this.monthNames[dateArray[i].getMonth()];	// @todo add date formatting

		var planeTexture = new BABYLON.DynamicTexture("dynamic texture", 1024, this.scene, true);
		planeTexture.hasAlpha = true;

		var planeMaterial = new BABYLON.StandardMaterial("plane material", this.scene);
		planeMaterial.backFaceCulling = false;
		planeMaterial.specularColor = new BABYLON.Color3(0, 0, 0);
		//planeMaterial.alpha = 0.5;
		planeTexture.drawText(text, null, 150, "bold 68px GothamPro", "white", null);

		planeMaterial.diffuseTexture = planeTexture;


		var plane = BABYLON.Mesh.CreatePlane("id_" + text, 20, this.scene);
		plane.material = planeMaterial;
		plane.rotation.x = this.correctRotation;
		plane.parent = parentObj;

		plane.scaling = new BABYLON.Vector3(1, 1, 0.05);
		plane.position = new BABYLON.Vector3(-5, -10, (i + 0.6) * this.vlaneInterval);

        this.createDatePinObject(i, parentObj);
	}
};

Builder.prototype.createDatePinObject = function (posZ, parentObj) {

    var border = BABYLON.Mesh.CreateSphere("dateObjSphere", 8, 0.5, this.scene);

    border.position = new BABYLON.Vector3(0, 0, posZ * this.vlaneInterval);
    border.parent = parentObj;
};

/*********************************
 refresh functions, with animation
 */


Builder.prototype.updateGrid = function (laneNum) {
    this.LANES_NUM = laneNum;

    // @todo animation
    for (var i = 0; i < this.hlanes.length; i++) {
        this.hlanes[i].dispose();
    }
    this.hlanes = [];

    for (var i = 0; i < this.vlanes.length; i++) {
        this.vlanes[i].dispose();
    }
    this.vlanes = [];

    this.subPlane.scaling.x = this.hlaneInterval * laneNum;
    this.subPlane.position.x = this.hlaneInterval * laneNum / 2;

    this.fadeIn(this.subPlane);

	this.currentPoint = this.createParentPointParent(0);
	this.dateCurrentPoint = this.createParentPointParent(0);
	this.createHLanes(laneNum, this.currentPoint);
	this.createDate(this.activeDate, this.dateCurrentPoint);

	this.nextPoint = this.createParentPointParent(this.hlines * this.vlaneInterval);
	this.dateNextPoint = this.createParentPointParent(this.hlines * this.vlaneInterval);
	this.createHLanes(laneNum, this.nextPoint);
	this.createDate(this.activeDate.addDays(this.hlines), this.dateNextPoint);

	this.prevPoint = this.createParentPointParent(this.hlines * this.vlaneInterval * -1);
	this.datePrevPoint = this.createParentPointParent(this.hlines * this.vlaneInterval * -1);
	this.createHLanes(laneNum, this.prevPoint);
	this.createDate(this.activeDate.addDays(this.hlines * -1), this.datePrevPoint);

    for (var i = 0; i < laneNum + 1; i++) {
        this.createVLane( i, i, 0);
    }

    this.camera.position.x = this.LEFT_CAM_LIMIT;

    this.RIGHT_CAM_LIMIT = this.hlaneInterval * (laneNum - 1);
};

Builder.prototype.resizeGrid = function () {
    // @todo animation resize
};


/*********************************
 axillary functions
 */

/**
 * infinite dispance objects with relative pos to camera
 * @param mesh
 */
Builder.prototype.setRelatedToCamPos = function (mesh) {
    mesh.position.z -= this.CAMERA_POS.z;
    mesh.infiniteDistance = true;
};

Builder.prototype.initClick = function() {
    this.audioClick.play();
};

/**
 * fade in animation
 * @param obj
 */
Builder.prototype.fadeIn = function (obj) {

    var keys = [];
    keys.push({frame: 0, value: 0});
    keys.push({frame: 50, value: 1});

    var alpha = new BABYLON.Animation('alpha', 'alpha', 50, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    alpha.setKeys(keys);

    keys = [];
    keys.push({frame: 0, value: -3});
    keys.push({frame: 50, value: 0});

    var yMvmnt = new BABYLON.Animation('yMvmnt', 'position.y', 50, BABYLON.Animation.ANIMATIONTYPE_FLOAT, BABYLON.Animation.ANIMATIONLOOPMODE_CYCLE);
    yMvmnt.setKeys(keys);

    obj.animations.push(alpha);
    obj.animations.push(yMvmnt);

    this.scene.beginAnimation(obj, 0, 50, false);
};


Builder.prototype.getDatesBetween = function(startDate, stopDate) {
    var dateArray = new Array();
    var currentDate = startDate;
    while (currentDate <= stopDate) {
        dateArray.push(currentDate)
        currentDate = currentDate.addDays(1);
    }
    return dateArray;
}

Date.prototype.addDays = function(days) {
    var dat = new Date(this.valueOf())
    dat.setDate(dat.getDate() + days);
    return dat;
};

/*********************************
 Action functions
 */

Builder.prototype.addPopup = function (id, eventid, mesh, popup) {
    mesh.actionManager = new BABYLON.ActionManager(this.scene);
    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPointerOverTrigger, function () {
        popup.mouseOverPopup(eventid);
    }));
    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPointerOutTrigger, function () {
        popup.popupHide();
    }));
    mesh.actionManager.registerAction(new BABYLON.ExecuteCodeAction(BABYLON.ActionManager.OnPickTrigger, function () {
        popup.showMinimap(id, eventid)
    }));
};

Builder.prototype.responseToCameraEvents = function (camSpeed)
{
    if (this.camera.position.x + camSpeed.x >= this.LEFT_CAM_LIMIT && this.camera.position.x + camSpeed.x <= this.RIGHT_CAM_LIMIT)
    {
        this.camera.position.x += camSpeed.x;

		var datesMovePoint = 5 * this.hlaneInterval;

		if(this.camera.position.x > datesMovePoint)
        {
			this.dateCurrentPoint.position.x += camSpeed.x;
		} else
        {
			this.dateCurrentPoint.position.x = 0;
		}

		this.dateNextPoint.position.x = this.dateCurrentPoint.position.x;
		this.datePrevPoint.position.x = this.dateCurrentPoint.position.x;
    }

    if (this.camera.position.z + camSpeed.z >= -500 && this.camera.position.z + camSpeed.z <= 10000) // @todo change movment limit
    {
        this.camera.position.z += camSpeed.z;

        //this.subPlane.position.z += camSpeed.z;
        this.zeroPointMovableObjs.position.z += camSpeed.z;
    }

    var renderPoint = this.currentPoint.position.z + (this.intervalPreloadPos * this.vlaneInterval);
    if (this.camera.position.z > renderPoint) {

		this.prevPoint.dispose();
		this.datePrevPoint.dispose();
		this.prevPoint = this.currentPoint;
		this.datePrevPoint = this.dateCurrentPoint;
		this.currentPoint = this.nextPoint;
		this.dateCurrentPoint = this.dateNextPoint;

		this.nextPoint = this.createParentPointParent(this.currentPoint.position.z + (this.hlines * this.vlaneInterval));
		this.dateNextPoint = this.createParentPointParent(this.currentPoint.position.z + (this.hlines * this.vlaneInterval), this.currentPoint.position.x);
		this.createHLanes(this.LANES_NUM, this.nextPoint);
		this.activeDate = this.activeDate.addDays(this.hlines);
		this.createDate(this.activeDate.addDays(this.hlines), this.dateNextPoint);
    }

    var deletePoint = this.prevPoint.position.z + (this.intervalPreloadPos * this.vlaneInterval);
    if (this.camera.position.z <= deletePoint) {

		this.nextPoint.dispose();
		this.dateNextPoint.dispose();
		this.nextPoint = this.currentPoint;
		this.dateNextPoint = this.dateCurrentPoint;
		this.currentPoint = this.prevPoint;
		this.dateCurrentPoint = this.datePrevPoint;

		this.prevPoint = this.createParentPointParent(this.currentPoint.position.z - (this.hlines * this.vlaneInterval));
		this.datePrevPoint = this.createParentPointParent(this.currentPoint.position.z - (this.hlines * this.vlaneInterval), this.currentPoint.position.x);
		this.createHLanes(this.LANES_NUM, this.prevPoint);
		this.activeDate = this.activeDate.addDays(this.hlines * -1);
		this.createDate(this.activeDate.addDays(this.hlines * -1), this.datePrevPoint);
    }

    var activeLanesNameId = this.lanesName.length - Math.floor(this.camera.position.x / this.hlaneInterval);
    var activeLaneName = this.lanesName[activeLanesNameId];
    if (undefined !== activeLaneName)
    {
        for (var i = activeLanesNameId - this.angleFactorWaveEffect - 1; i <= activeLanesNameId + this.angleFactorWaveEffect; i++)
        {
            var paralelLaneName = this.lanesName[i];
            if (undefined !== paralelLaneName)
            {
                this.issueNamesAnimation(paralelLaneName);
            }
        }

    }
};

/**
 * animation of issue objects
 * @param issueObj
 */
Builder.prototype.issueNamesAnimation = function (issueObj)
{
    var positionInterval = Math.abs(this.camera.position.x - issueObj.position.x) / this.hlaneInterval;

    var minRotate = Math.PI / 2;

    var positionPercent = (this.angleFactorWaveEffect - positionInterval) / this.angleFactorWaveEffect;
    var totalRotate = positionPercent * (minRotate - this.correctRotation);

    if( totalRotate < 0 ) {
        issueObj.rotation.x = minRotate;
        return;
    }

    issueObj.rotation.x = minRotate - totalRotate;
};
