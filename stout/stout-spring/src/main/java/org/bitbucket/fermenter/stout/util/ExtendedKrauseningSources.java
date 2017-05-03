package org.bitbucket.fermenter.stout.util;

import java.lang.annotation.Annotation;

import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

public class ExtendedKrauseningSources implements KrauseningSources {
	
	private String[] value;
	
	public ExtendedKrauseningSources(String value) {
		this.value = new String[]{value};
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ExtendedKrauseningSources.class;
	}

	@Override
	public String[] value() {
		return this.value;
	}
	
}