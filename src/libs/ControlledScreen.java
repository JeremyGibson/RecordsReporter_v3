package libs;

import LoginMod.classes.User;

/**
 * Created by jgibson on 8/26/2014.
 */
public interface ControlledScreen {
    public void setScreenParent(ScreenViewSwitcher screenPage);
    public void setUser(User u);
    public void init();
}
