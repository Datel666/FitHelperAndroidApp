package pr.code.views.helper;

import pr.code.models.Recomendations;
import pr.code.models.UserInfo;
import java.util.List;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface HelperView {

    void setUserInfo(List<UserInfo> userInfoList);

    void setRecomendations(List<Recomendations.Recomendation> recomendationsList);


}
