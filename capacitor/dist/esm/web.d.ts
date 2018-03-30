import { WebPlugin } from '@capacitor/core';
export declare class MyPluginWeb extends WebPlugin {
    constructor();
    echo(options: {
        value: string;
    }): Promise<void>;
}
declare const MyPlugin: MyPluginWeb;
export { MyPlugin };
