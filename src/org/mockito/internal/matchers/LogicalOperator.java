/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

public enum LogicalOperator {
    LESS_THAN("<") {
        public boolean matchResult(int result) {
            return result < 0;
        }
    },
    LESS_OR_EQUAL("<=") {
        public boolean matchResult(int result) {
            return result <= 0;
        }
    },
    EQUAL("==") {
        public boolean matchResult(int result) {
            return result == 0;
        }
    },
    GREATER_OR_EQUAL(">=") {
        public boolean matchResult(int result) {
            return result >= 0;
        }
    },
    GREATER(">") {
        public boolean matchResult(int result) {
            return result > 0;
        }
    };
    
    private String symbol;
    
    private LogicalOperator(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public abstract boolean matchResult(int result);
}
