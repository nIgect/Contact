package com.itechart.tarasevi.logic.processcommand;

import com.itechart.tarasevi.logic.commands.EmptyCommand;
import com.itechart.tarasevi.logic.commands.addcommands.AddAttachmentCommand;
import com.itechart.tarasevi.logic.commands.addcommands.AddPhotoCommand;
import com.itechart.tarasevi.logic.commands.deletecommands.DeleteAttachmentCommand;
import com.itechart.tarasevi.logic.commands.deletecommands.DeleteCommand;
import com.itechart.tarasevi.logic.commands.deletecommands.DeletePhotoCommand;
import com.itechart.tarasevi.logic.commands.editcommands.EditAttachmentCommand;
import com.itechart.tarasevi.logic.commands.emailcommand.EmailCommand;
import com.itechart.tarasevi.logic.commands.emailcommand.SendMailCommand;
import com.itechart.tarasevi.logic.commands.emailcommand.template.ApplyTemplateEmailCommand;
import com.itechart.tarasevi.logic.commands.maincommands.*;

public enum CommandEnum {
    NEW_FILE {
        {
            this.command = new AddAttachmentCommand();
        }
    },
    APPLY_TEMPLATE_EMAIL_COMMAND {
        {
            this.command = new ApplyTemplateEmailCommand();
        }
    },
    CONTACT {
        {
            this.command = new ContactCommand();
        }
    },
    DELETE {
        {
            this.command = new DeleteCommand();
        }
    },
    DELETEATTACHFILE {
        {
            this.command = new DeleteAttachmentCommand();
        }
    },
    DELETE_PHOTO {
        {
            this.command = new DeletePhotoCommand();
        }
    },
    EMAIL {
        {
            this.command = new EmailCommand();
        }
    },
    EDIT {
        {
            this.command = new EditCommand();
        }
    },
    EDIT_FILE {
        {
            this.command = new EditAttachmentCommand();
        }
    },
    EMPTY {
        {
            this.command = new EmptyCommand();
        }
    },
    NEW {
        {
            this.command = new NewCommand();
        }
    },
    SAVE {
        {
            this.command = new SaveCommand();
        }
    },
    SAVEPHOTO {
        {
            this.command = new AddPhotoCommand();
        }
    },
    SEARCH {
        {
            this.command = new SearchCommand();
        }
    },
    SENDEMAIL {
        {
            this.command = new SendMailCommand();
        }
    },
    UPDATE_ATTACHMENT {
        {
            this.command = new UpdateCommand();
        }
    },
    UPDATE_PHONE {
        {
            this.command = new UpdateCommand();
        }
    },
    UPDATE_PHOTO {
        {
            this.command = new UpdateCommand();
        }
    };

    ActionCommand command;

    public ActionCommand getCurrentCommand() {
        return this.command;
    }


}
