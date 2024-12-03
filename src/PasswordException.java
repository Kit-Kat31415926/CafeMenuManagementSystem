public class PasswordException extends Exception {
	
    public PasswordException(String message) {
        super(message);
    }
}
class UpperCaseCharacterMissing extends PasswordException {
    public UpperCaseCharacterMissing() {
        super("Password must contain at least one uppercase letter.");
    }
}

class LowerCaseCharacterMissing extends PasswordException {
    public LowerCaseCharacterMissing() {
        super("Password must contain at least one lowercase letter.");
    }
}

class SpecialCharacterMissing extends PasswordException {
    public SpecialCharacterMissing() {
        super("Password must contain at least one special character like !@#$%^&*()");
    }
}

class NumberCharacterMissing extends PasswordException {
    public NumberCharacterMissing() {
        super("Password must contain at least one number.");
    }
}

class Minimum8CharactersRequired extends PasswordException {
    public Minimum8CharactersRequired() {
        super("Password must be at least 8 characters long.");
    }
}