package pr.code.views.helper.recomendations;

import pr.code.models.Recomendations;
import pr.code.models.UserInfo;
import java.util.List;

/**
 * View interface that used to contact Presenter for updates
 */
public interface HelperView {

    void setUserInfo(List<UserInfo> userInfoList);

    void setRecomendations(List<Recomendations.Recomendation> recomendationsList);
    void onErrorLoading(String message);

}
