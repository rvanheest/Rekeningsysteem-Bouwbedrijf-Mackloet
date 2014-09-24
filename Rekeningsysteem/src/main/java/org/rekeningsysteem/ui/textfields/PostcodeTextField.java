package org.rekeningsysteem.ui.textfields;

import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;

public class PostcodeTextField extends TextField {

	public PostcodeTextField() {
		super();
		this.setPromptText("1234AB");
	}
	
	@Override
	public void replaceText(int start, int end, String text) {
		if ((this.getText() + text).length() + start - end <= 6) {
    		if (start < end && text.isEmpty()) {
    			// backspace/delete
    			super.replaceText(start, end, text);
    		}
    		else if (start >= 0 && start <= 3 && text.matches("[0-9]")) {
    			super.replaceText(start, end, text);
    		}
    		else if (start >= 4 && start <= 5 & text.matches("[a-zA-Z]")) {
    			super.replaceText(start, end, text.toUpperCase());
    		}
		}
	}

	@Override
	public void replaceSelection(String text) {
		IndexRange selection = this.getSelection();
		if ((this.getText() + text).length() - selection.getLength() <= 6) {
			int start = selection.getStart();
			int end = selection.getEnd();
			
    		if (start < end && text.isEmpty()) {
    			// backspace/delete
    			super.replaceSelection(text);
    		}
    		else if (start >= 0 && start <= 3 && text.matches("[0-9]")) {
    			super.replaceSelection(text);
    		}
    		else if (start >= 4 && start <= 5 & text.matches("[a-zA-Z]")) {
    			super.replaceSelection(text.toUpperCase());
    		}
		}
	}
}
