var EventListener = function (builder, drawPointer) {
    this.drawPointer = drawPointer;
    this.builder = builder;

    this.leapCtrl;

    this.mouseX;
    this.mouseY;

    this.appX;
    this.appY;

    this.MOVE_CAMERA = false;

    this.camSpeed = {x: 0, z: 0};
    this.movementSpeed = 0.5;
    this.deAcceleration = 0.1;

    this.PINCH_RELEASED = true;
};

EventListener.prototype.addLeapMotion = function ( leapCtrl ) {
    this.leapCtrl = leapCtrl;
    var self = this;
    this.leapCtrl.on( 'connect', function() {
        setInterval(function() {
            if( self.leapCtrl.frame() != null )
            {
                self.leapMovement(self.leapCtrl.frame());
            }
            else
            {
                self.drawPointer(null);
            }
        }, 50);
    } );
};

EventListener.prototype.addMouseListener = function ( object ) {
    var self = this;
    var canvas = document.getElementById("renderCanvas");

    object.onmousedown = function(e){
        var pos = $(canvas).offset();

        self.mouseX = e.pageX - pos.left;
        self.mouseY = e.pageY - pos.top;

        canvas.onmousemove = mouseMove;
        canvas.onmouseup = mouseUp;

        this.MOVE_CAMERA = true;
    };

    function mouseUp(e){

        canvas.onmousemove = null;
        self.camSpeed.x = 0;
        self.camSpeed.y = 0;


        this.MOVE_CAMERA = false;
    };

    function mouseMove(e)
    {
        var pos = $(canvas).offset();
        var x = e.pageX - pos.left;
        var y = e.pageY - pos.top;

        self.camSpeed.x = ( self.mouseX - x ) / 50;
        self.camSpeed.z = ( y - self.mouseY ) / 50;

        return false;
    }
};

EventListener.prototype.movePointer = function (frame, finger)
{
    if (Math.abs(finger.direction[0]) > 0.5 || Math.abs(finger.direction[1]) > 0.5) {
        this.drawPointer(frame);
        return;
    }

    if( finger.tipVelocity[2] > 500 ) {
        this.builder.initClick();
    }

    var appWidth = window.innerWidth;
    var appHeight = window.innerHeight;

    var iBox = frame.interactionBox;


    var normalizedPoint = iBox.normalizePoint(finger.stabilizedTipPosition, true);

    this.appX = normalizedPoint[0] * appWidth;
    this.appY = (1 - normalizedPoint[1]) * appHeight;

    this.drawPointer(frame);
};

EventListener.prototype.leapMovement = function (frame) {
    var hand = frame.hands[0];

    if (hand) {

        this.movePointer(frame, hand.indexFinger);

        if (frame.hands[0].pinchStrength > 0.4 && hand.indexFinger.direction[1] < -0.37 ) {
            if (this.PINCH_RELEASED) {
                this.camSpeed.x = 0;
                this.camSpeed.z = 0;
                this.PINCH_RELEASED = false;
            }

            if (Math.abs(parseInt(hand.palmVelocity[0] / 10)) > 0) {
                this.camSpeed.x -= hand.palmVelocity[0] / 1000 * this.movementSpeed;
            }


            if (Math.abs(parseInt(hand.palmVelocity[2] / 10)) > 0) {
                this.camSpeed.z += hand.palmVelocity[2] / 1000 * this.movementSpeed;
            }
        }
        else {
            this.PINCH_RELEASED = true;

            if (Math.abs(this.camSpeed.x) - this.deAcceleration > 0) {
                this.camSpeed.x -= this.camSpeed.x > 0 ? 1 : -1 * this.deAcceleration;
            }
            else {
                this.camSpeed.x = 0;
            }

            if (Math.abs(this.camSpeed.z) - this.deAcceleration > 0) {
                this.camSpeed.z -= this.camSpeed.z > 0 ? 1 : -1 * this.deAcceleration;
            }
            else {
                this.camSpeed.z = 0;
            }
        }
    }
    else {
        this.PINCH_RELEASED = true;

        if (Math.abs(this.camSpeed.x) - this.deAcceleration > 0) {
            this.camSpeed.x -= this.camSpeed.x > 0 ? 1 : -1 * this.deAcceleration;
        }
        else {
            this.camSpeed.x = 0;
        }

        if (Math.abs(this.camSpeed.z) - this.deAcceleration > 0) {
            this.camSpeed.z -= this.camSpeed.z > 0 ? 1 : -1 * this.deAcceleration;
        }
        else {
            this.camSpeed.z = 0;
        }
    }
};

EventListener.prototype.update = function () {
    this.builder.responseToCameraEvents(this.camSpeed);
};
