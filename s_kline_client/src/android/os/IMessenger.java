package android.os;

public abstract class IMessenger implements IInterface{

	@Override
	public abstract void send(Message msg);

}
