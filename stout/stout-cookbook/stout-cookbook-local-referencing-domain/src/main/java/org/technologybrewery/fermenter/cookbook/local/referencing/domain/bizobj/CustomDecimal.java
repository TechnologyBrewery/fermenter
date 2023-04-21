package org.technologybrewery.fermenter.cookbook.local.referencing.domain.bizobj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Demonstrative class to show how you can override a type.
 */
public class CustomDecimal extends BigDecimal {

    private static final long serialVersionUID = 1L;

    public CustomDecimal(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public CustomDecimal(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public CustomDecimal(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public CustomDecimal(BigInteger val) {
        super(val);
    }

    public CustomDecimal(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public CustomDecimal(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public CustomDecimal(char[] in, MathContext mc) {
        super(in, mc);
    }

    public CustomDecimal(char[] in) {
        super(in);
    }

    public CustomDecimal(double val, MathContext mc) {
        super(val, mc);
    }

    public CustomDecimal(double val) {
        super(val);
    }

    public CustomDecimal(int val, MathContext mc) {
        super(val, mc);
    }

    public CustomDecimal(int val) {
        super(val);
    }

    public CustomDecimal(long val, MathContext mc) {
        super(val, mc);
    }

    public CustomDecimal(long val) {
        super(val);
    }

    public CustomDecimal(String val, MathContext mc) {
        super(val, mc);
    }

    public CustomDecimal(String val) {
        super(val);
    }

}
