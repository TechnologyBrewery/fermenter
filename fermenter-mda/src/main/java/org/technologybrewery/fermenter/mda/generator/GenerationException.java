package org.technologybrewery.fermenter.mda.generator;

public class GenerationException extends RuntimeException {

    private static final long serialVersionUID = -3898544317651420802L;

    public GenerationException() {
		super();
	}

	public GenerationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GenerationException(String arg0) {
		super(arg0);
	}

	public GenerationException(Throwable arg0) {
		super(arg0);
	}

}
