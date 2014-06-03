package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;

import org.kesler.simplereg.logic.reception.filter.*;


public abstract class ReceptionsFilterDialogFactory {

    /**
     * Создает диалог создания нового фильтра
     * @param view родительский вид
     * @param filterEnum тип диалога
     * @return  диалог создания фильтра
     */
	public static ReceptionsFilterDialog createDialog(JFrame view, ReceptionsFiltersEnum filterEnum) {
		ReceptionsFilterDialog receptionsFilterDialog = null;
		/// Создаем диалог на основнии сведений о необходимом типе диалога
		switch (filterEnum) {
			case OPEN_DATE:
				receptionsFilterDialog = new OpenDateReceptionsFilterDialog(view);
			break;
            case FL:
                receptionsFilterDialog = new FLReceptionsFilterDialog(view);
                break;
            case UL:
                receptionsFilterDialog = new ULReceptionsFilterDialog(view);
                break;
			case FILIAL:
				receptionsFilterDialog = new FilialReceptionsFilterDialog(view);
			break;
			case BY_RECORD:
				receptionsFilterDialog = new ByRecordReceptionsFilterDialog(view);
			break;
			case STATUS:
				receptionsFilterDialog = new StatusReceptionsFilterDialog(view);	
			break;
			case SERVICE:
				receptionsFilterDialog = new ServiceReceptionsFilterDialog(view);	
			break;
			case OPERATOR:
				receptionsFilterDialog = new OperatorReceptionsFilterDialog(view);	
			break;
			case TO_ISSUE_DATE:
				receptionsFilterDialog = new ToIssueDateReceptionsFilterDialog(view);	
			break;
			case RESULT_IN_MFC:
				receptionsFilterDialog = new ResultInMFCReceptionsFilterDialog(view);	
			break;
			default:
				break;
		}

		return receptionsFilterDialog;
	}

    /**
     * Создает диалог редактирования фильтра
     * @param view
     * @param receptionsFilter
     * @return
     */
	public static ReceptionsFilterDialog createDialog(JFrame view, ReceptionsFilter receptionsFilter) {

		ReceptionsFilterDialog receptionsFilterDialog = createDialog(view, receptionsFilter.getFiltersEnum());
        if(receptionsFilterDialog!=null)
            receptionsFilterDialog.setReceptionsFilter(receptionsFilter);

		return receptionsFilterDialog;

	}



}