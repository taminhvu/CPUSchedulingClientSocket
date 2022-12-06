package entities;

import javax.crypto.SealedObject;
import java.io.Serializable;

public class DataPackage implements Serializable {
	private SealedObject sealedObject;
	private String message;
	public DataPackage(SealedObject sealedObject, String message){
		this.sealedObject = sealedObject;
		this.message = message;
	}

	public SealedObject getSealedObject() {
		return sealedObject;
	}

	public void setSealedObject(SealedObject sealedObject) {
		this.sealedObject = sealedObject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
