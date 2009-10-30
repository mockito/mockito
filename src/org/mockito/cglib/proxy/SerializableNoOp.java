package org.mockito.cglib.proxy;

import java.io.Serializable;

public class SerializableNoOp implements NoOp, Serializable {

  private static final long serialVersionUID = 7434976328690189159L;
  public static final Callback SERIALIZABLE_INSTANCE = new SerializableNoOp();

}
