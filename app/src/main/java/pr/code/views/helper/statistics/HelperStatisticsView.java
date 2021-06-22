package pr.code.views.helper.statistics;

import java.util.List;

import pr.code.models.StatisticsInfo;

/**
 * View interface that used to contact Presenter for updates
 */
public interface HelperStatisticsView {

    void setFormsInfo(List<StatisticsInfo> templist);
    void onErrorLoading(String message);
}
