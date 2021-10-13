package taa.command;

import taa.storage.Storage;
import taa.exception.TaaException;
import taa.Ui;
import taa.module.ModuleList;
import taa.module.Module;

public class AddModuleCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_MODULE_NAME = "n";
    private static final String[] ADD_MODULE_ARGUMENT_KEYS = {KEY_MODULE_CODE, KEY_MODULE_NAME};

    private static final String MESSAGE_INVALID_MODULE_CODE = "Invalid module code.";

    private static final String MESSAGE_FORMAT_ADD_MODULE_USAGE = "Usage: %s "
            + "%s/<MODULE_CODE> %s/<MODULE_NAME>";
    private static final String MESSAGE_FORMAT_MODULE_ADDED = "Module added:\n  %s\nThere are %d modules in the list.";

    public AddModuleCommand(String argument) {
        super(argument, ADD_MODULE_ARGUMENT_KEYS);
    }

    /**
     * Executes the add_module command and adds a module.
     *
     * @param moduleList The list of modules.
     * @param ui         The ui instance to handle interactions with the user.
     * @param storage    The storage instance to handle saving.
     * @throws TaaException If the user inputs an invalid command or has missing/invalid argument(s).
     */
    @Override
    public void execute(ModuleList moduleList, Ui ui, Storage storage) throws TaaException {
        if (argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }

        if (!checkArguments()) {
            throw new TaaException(getMissingArgumentMessage());
        }

        assert argumentMap.containsKey(KEY_MODULE_CODE);
        String moduleCode = argumentMap.get(KEY_MODULE_CODE);
        if (moduleCode.contains(" ")) {
            throw new TaaException(MESSAGE_INVALID_MODULE_CODE);
        }

        assert argumentMap.containsKey(KEY_MODULE_NAME);
        String name = argumentMap.get(KEY_MODULE_NAME);
        Module module = new Module(moduleCode, name);
        moduleList.addModule(module);

        storage.save(moduleList);

        ui.printMessage(String.format(MESSAGE_FORMAT_MODULE_ADDED, module, moduleList.getSize()));
    }

    @Override
    protected String getUsageMessage() {
        return String.format(
                MESSAGE_FORMAT_ADD_MODULE_USAGE,
                COMMAND_ADD_MODULE,
                KEY_MODULE_CODE,
                KEY_MODULE_NAME
        );
    }
}
