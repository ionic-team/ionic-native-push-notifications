import {mergeWebPlugins, Plugins} from '@capacitor/core';

export * from './definitions';
export * from './web';

mergeWebPlugins(Plugins);
