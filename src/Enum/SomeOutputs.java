package Enum;

public enum SomeOutputs {

    INVAILID_USERNAME_FORMAT("username format is invalid"),
    INVALID_PASSWORD_FORMAT("password format is invalid"),
    USERNAME_HAS_USED("a user exists with this username"),
    INVALID_USERNAME("no user exists with this username"),
    INCORRECT_PASSWORD("incorrect password"),
    SUCCESSFULLY_REGISTERED("register successful"),
    SUCCESSFULLY_LOGGEDIN("login successful"),
    INVALID_COMMAND("invalid command"),
    SUCCESSFULLY_LOGGEDOUT("logout successful"),
    PROGRAM_ENDED("program ended"),
    IT_IS_YOU("you must choose another player to start a game"),
    INVALID_SHIP_NUMBER("invalid ship number"),
    WORNG_COORDINATION("wrong coordination"),
    INVALID_DIRECTION("invalid direction"),
    OFF_THE_BOARD("off the board"),
    DONT_HAVE_THIS_TYPE_OF_SHIP("you don't have this type of ship"),
    COLLISION_WITH_OTHER_THING("collision with the other ship or mine on the board"),
    DONT_HAVE_ENOUTH_MINE("you don't have enough mine"),
    DONT_HAVE_ENOUTH_ANTI_AIRCRAFT("you don't have enough antiaircraft"),
    DONT_HAVE_ENOUTH_INVISIBLE("you don't have enough invisible"),
    NO_SHIP_IN_THIS_PLACE("there is no ship on this place on the board"),
    ALREADY_MADE_INVISIBLE("this place has already made invisible"),
    TURN_COMPLETED("turn completed")
    ;

    String value;

    SomeOutputs(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
