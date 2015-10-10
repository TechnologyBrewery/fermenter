package org.bitbucket.fermenter.mda.generator;

public interface Generator {

	public void generate(GenerationContext context) throws GenerationException;
	
}
