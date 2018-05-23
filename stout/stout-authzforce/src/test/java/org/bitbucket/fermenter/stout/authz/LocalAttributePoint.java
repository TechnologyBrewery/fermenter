package org.bitbucket.fermenter.stout.authz;

import java.util.Date;

/**
 * Test attribute point that just does some local logic to support test cases.
 */
public class LocalAttributePoint implements StoutAttributePoint {

    @Override
    public AttributeValue<?> getValueForAttribute(String attributeId, String subject) {
        AttributeValue<?> value = null;
        if ("urn:stout:jerseyNumber".equals(attributeId)) {
            value = sourceJerseyNumberAttribute(attributeId, subject, value);
        } else if ("urn:stout:hallOfFameProfile".equals(attributeId)) {
            value = sourceHallOfFameProfileAttribute(attributeId, subject, value);
        } else if ("urn:stout:suspectedPedUser".equals(attributeId)) {
            value = sourceSuspectedPedUserAttribute(attributeId, subject, value);
        } else if ("urn:stout:battingAverage".equals(attributeId)) {
            value = sourceBattingAverageAttribute(attributeId, subject, value);
        } else if ("urn:stout:serviceEntryDate".equals(attributeId)) {
            value = sourceServiceEntryDateAttribute(attributeId, subject, value);
        }

        return value;
    }

    protected AttributeValue<?> sourceJerseyNumberAttribute(String attributeId, String subject,
            AttributeValue<?> value) {
        if ("reggieJackson".equals(subject)) {
            value = new AttributeValue<Integer>(attributeId, 44);
        } else if ("tonyGwynn".equals(subject)) {
            value = new AttributeValue<Integer>(attributeId, 19);
        } else if ("alexOvechkin".equals(subject)) {
            value = new AttributeValue<Integer>(attributeId, 8);
        } else if ("anthonyRizzo".equals(subject)) {
            value = new AttributeValue<Integer>(attributeId, 44);
        }

        return value;
    }

    protected AttributeValue<?> sourceHallOfFameProfileAttribute(String attributeId, String subject,
            AttributeValue<?> value) {
        if ("reggieJackson".equals(subject)) {
            value = new AttributeValue<String>(attributeId, "https://baseballhall.org/hall-of-famers/jackson-reggie");
        } else if ("tonyGwynn".equals(subject)) {
            value = new AttributeValue<String>(attributeId, "https://baseballhall.org/hall-of-famers/gwynn-tony");
        } else if ("marioMendoza".equals(subject)) {
            value = new AttributeValue<String>(attributeId, "https://baseballhall.mx/mexican-hall-of-famers/mendoza");
        } else if ("alexOvechkin".equals(subject)) {
            value = new AttributeValue<String>(attributeId, "https://www.nhl.com/news/alex-ovechkin-100-greatest");
        }

        return value;
    }

    protected AttributeValue<?> sourceSuspectedPedUserAttribute(String attributeId, String subject,
            AttributeValue<?> value) {
        if ("reggieJackson".equals(subject)) {
            value = new AttributeValue<Boolean>(attributeId, Boolean.FALSE);
        } else if ("tonyGwynn".equals(subject)) {
            value = new AttributeValue<Boolean>(attributeId, Boolean.FALSE);
        } else if ("kenCaminiti".equals(subject)) {
            value = new AttributeValue<Boolean>(attributeId, Boolean.TRUE);
        } else if ("wallyJoyner".equals(subject)) {
            value = new AttributeValue<Boolean>(attributeId, Boolean.TRUE);
        }

        return value;
    }

    protected AttributeValue<?> sourceBattingAverageAttribute(String attributeId, String subject,
            AttributeValue<?> value) {
        if ("reggieJackson".equals(subject)) {
            value = new AttributeValue<Double>(attributeId, new Double(.262));
        } else if ("tonyGwynn".equals(subject)) {
            value = new AttributeValue<Double>(attributeId, new Double(.338));
        } else if ("kenCaminiti".equals(subject)) {
            value = new AttributeValue<Double>(attributeId, new Double(.272));
        } else if ("marioMendoza".equals(subject)) {
            value = new AttributeValue<Double>(attributeId, new Double(.215));
        }

        return value;
    }

    protected AttributeValue<?> sourceServiceEntryDateAttribute(String attributeId, String subject,
            AttributeValue<?> value) {
        if ("reggieJackson".equals(subject)) {
            value = new AttributeValue<Date>(attributeId, new Date(-78364800000L));
        } else if ("tonyGwynn".equals(subject)) {
            value = new AttributeValue<Date>(attributeId, new Date(395971200000L));
        } else if ("kenCaminiti".equals(subject)) {
            value = new AttributeValue<Date>(attributeId, new Date(553392000000L));
        } else if ("marioMendoza".equals(subject)) {
            value = new AttributeValue<Date>(attributeId, new Date(136166400000L));
        }

        return value;
    }

}
