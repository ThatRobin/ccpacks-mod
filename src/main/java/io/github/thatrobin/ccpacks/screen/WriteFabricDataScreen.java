package io.github.thatrobin.ccpacks.screen;

import io.github.thatrobin.ccpacks.util.CCPackInfo;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class WriteFabricDataScreen extends Screen {
    private static final Text ENTER_NAME_TEXT = new TranslatableText("write_data.enter_name");
    private static final Text ENTER_AUTHOR_TEXT = new TranslatableText("write_data.enter_author");
    private static final Text ENTER_ID_TEXT = new TranslatableText("write_data.enter_id");
    private static final Text ENTER_DESCRIPTION_TEXT = new TranslatableText("write_data.enter_description");
    private static final Text ENTER_VERSION_TEXT = new TranslatableText("write_data.enter_version");
    private static final Text ENTER_LICENSE_TEXT = new TranslatableText("write_data.enter_license");
    private ButtonWidget addButton;
    private final BooleanConsumer callback;
    private final CCPackInfo packInfo;
    private TextFieldWidget nameField;
    private TextFieldWidget idField;
    private TextFieldWidget versionField;
    private TextFieldWidget authorField;
    private TextFieldWidget descriptionField;
    private TextFieldWidget licenseField;
    private final Screen parent;

    public WriteFabricDataScreen(Screen parent, BooleanConsumer callback, CCPackInfo packInfo, String title) {
        super(new LiteralText("Would you like to compress "+title+" into a mod?"));
        this.parent = parent;
        this.callback = callback;
        this.packInfo = packInfo;
    }

    public void tick() {
        this.nameField.tick();
        this.idField.tick();
        this.versionField.tick();
        this.authorField.tick();
        this.descriptionField.tick();
        this.licenseField.tick();
    }

    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.nameField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) - (this.width / 4), 66, 200, 20, new TranslatableText("addServer.enterName"));
        this.nameField.setTextFieldFocused(true);
        this.nameField.setText(this.packInfo.name);
        this.nameField.setChangedListener((serverName) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.nameField);
        this.idField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) - (this.width / 4), 106, 200, 20, new TranslatableText("addServer.enterIp"));
        this.idField.setMaxLength(128);
        this.idField.setText(this.packInfo.id);
        this.idField.setChangedListener((address) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.idField);
        this.versionField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) - (this.width / 4), 146, 200, 20, new TranslatableText("addServer.enterVersion"));
        this.versionField.setMaxLength(128);
        this.versionField.setText(this.packInfo.version);
        this.versionField.setChangedListener((address) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.versionField);
        this.authorField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) + (this.width / 4), 66, 200, 20, new TranslatableText("addServer.enterName"));
        this.authorField.setMaxLength(128);
        this.authorField.setText(this.packInfo.author);
        this.authorField.setChangedListener((serverName) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.authorField);
        this.descriptionField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) + (this.width / 4), 106, 200, 20, new TranslatableText("addServer.enterIp"));
        this.descriptionField.setMaxLength(128);
        this.descriptionField.setText(this.packInfo.description);
        this.descriptionField.setChangedListener((address) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.descriptionField);
        this.licenseField = new TextFieldWidget(this.textRenderer, (this.width / 2 - 100) + (this.width / 4), 146, 200, 20, new TranslatableText("addServer.enterVersion"));
        this.licenseField.setMaxLength(128);
        this.licenseField.setText(this.packInfo.license);
        this.licenseField.setChangedListener((address) -> {
            this.updateAddButton();
        });
        this.addSelectableChild(this.licenseField);

        this.addButton = this.addDrawableChild(new ButtonWidget((this.width / 2 - 100), this.height / 4 + 96 + 18, 200, 20, new TranslatableText("addServer.add"), (button) -> {
            this.addAndClose();
        }));
        this.addDrawableChild(new ButtonWidget((this.width / 2 - 100), this.height / 4 + 120 + 18, 200, 20, ScreenTexts.CANCEL, (button) -> {
            this.callback.accept(false);
        }));
        this.updateAddButton();
    }

    public void resize(MinecraftClient client, int width, int height) {
        String string = this.nameField.getText();
        String string2 = this.idField.getText();
        String string3 = this.versionField.getText();
        String string4 = this.authorField.getText();
        String string5 = this.descriptionField.getText();
        String string6 = this.licenseField.getText();
        this.init(client, width, height);
        this.nameField.setText(string);
        this.idField.setText(string2);
        this.versionField.setText(string3);
        this.authorField.setText(string4);
        this.descriptionField.setText(string5);
        this.licenseField.setText(string6);
    }

    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
    }

    private void addAndClose() {
        this.packInfo.name = this.nameField.getText();
        this.packInfo.id = this.idField.getText();
        this.packInfo.version = this.versionField.getText();
        this.packInfo.author = this.authorField.getText();
        this.packInfo.description = this.descriptionField.getText();
        this.packInfo.license = this.licenseField.getText();
        this.callback.accept(true);
    }

    public void onClose() {
        this.client.setScreen(this.parent);
    }

    private void updateAddButton() {
        this.addButton.active = !this.nameField.getText().isEmpty() && !this.idField.getText().isEmpty() && !this.versionField.getText().isEmpty() && !this.authorField.getText().isEmpty() && !this.descriptionField.getText().isEmpty() && !this.licenseField.getText().isEmpty();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_NAME_TEXT, (this.width / 2 - 100) - (this.width / 4), 53, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_AUTHOR_TEXT, (this.width / 2 - 100) + (this.width / 4), 53, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_ID_TEXT, (this.width / 2 - 100) - (this.width / 4), 94, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_DESCRIPTION_TEXT, (this.width / 2 - 100) + (this.width / 4), 94, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_VERSION_TEXT, (this.width / 2 - 100) - (this.width / 4), 135, 10526880);
        drawTextWithShadow(matrices, this.textRenderer, ENTER_LICENSE_TEXT, (this.width / 2 - 100) + (this.width / 4), 135, 10526880);
        this.nameField.render(matrices, mouseX, mouseY, delta);
        this.idField.render(matrices, mouseX, mouseY, delta);
        this.versionField.render(matrices, mouseX, mouseY, delta);
        this.authorField.render(matrices, mouseX, mouseY, delta);
        this.descriptionField.render(matrices, mouseX, mouseY, delta);
        this.licenseField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
