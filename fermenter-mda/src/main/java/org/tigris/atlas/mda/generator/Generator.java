package org.tigris.atlas.mda.generator;

public interface Generator {

	public void generate(GenerationContext context) throws GenerationException;
	
}
