package pr.code.views.helper;

import pr.code.models.UserInfo;
import java.util.List;

public interface HelperView {

    void setUserInfo(List<UserInfo> userInfoList);

    void setRecomendations(List<String> recomendationsList);
}
