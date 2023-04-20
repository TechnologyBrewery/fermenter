package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.HashMap;
import java.util.Map;

/**
 * These transaction attribute constants are listed exactly as they are specified by the JEE specification. They are
 * uppercased to eliminate the possibility of case-related issues when reading and translating these values.
 */
public enum Transaction {
    REQUIRED("Required"), REQUIRES_NEW("RequiresNew"), MANDATORY("Mandatory"), NOT_SUPPORTED("NotSupported"), SUPPORTS(
            "Supports"), NEVER("Never");

    private String jtaName;
    private static Map<String, Transaction> jtaNameMap = getJtaNameMap();

    private Transaction(String jtaName) {
        this.jtaName = jtaName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return jtaName;
    }

    /**
     * Returns whether or no the the transaction is a valid JTA name.
     * 
     * @param transactionName
     *            JTA name
     * @return whether it is valid
     */
    public static boolean isValidTransaction(String transactionName) {
        return jtaNameMap.containsKey(transactionName);
    }

    /**
     * Returns the enum for the given JTA name.
     * 
     * @param transactionName
     *            JTA name
     * @return matching enum or null if not found.
     */
    public static Transaction getTransactionByJtaName(String transactionName) {
        return jtaNameMap.get(transactionName);
    }

    private static Map<String, Transaction> getJtaNameMap() {
        if (jtaNameMap == null) {
            jtaNameMap = new HashMap<>();
            for (Transaction transaction : Transaction.values()) {
                jtaNameMap.put(transaction.jtaName, transaction);
            }

        }

        return jtaNameMap;

    }

}
