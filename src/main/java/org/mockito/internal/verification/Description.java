package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Description verification mode wraps an existing verification mode and prepends
 * a custom message to the assertion error if verification fails.
 * @author Geoff.Schoeman
 * @since 2.0.0
 */
public class Description implements VerificationMode {

    private final VerificationMode verification;
    private final String description;
    
    /**
     * Constructs a verification mode which wraps the given verification mode. 
     * @param verification The implementation to use for verification
     * @param description The failure message to prepend if verification fails
     */
    public Description(VerificationMode verification, String description) {
        this.verification = verification;
        this.description = description;
    }
    
    /**
     * Performs verification using the wrapped verification mode implementation.
     * Prepends the custom failure message if verification fails.
     * @param data the data to be verified
     */
    @Override
    public void verify(VerificationData data) {
        try {
            verification.verify(data);
            
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError(e, description);
        }
    } 

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
