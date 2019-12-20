# illegalaccess-thread
monitor and adjust thread pool

## module description
1. illegal-thread-sdk, report thread pool metric, update thread pool via notification, provide thread pool creation ability.   
2. illegal-thread-meta, provide thread pool info, collect thread pool metric and do alarm, and notify config change.  
3. illegal-thread-admin, view and manage thread pool info.   

## function
1. sdk side    
1.1 it provides customized thread pool creator    
1.2 report thread pool runtime meta data to server    
2. server side   
2.1 it collect reported meta data, and can do alert according to configuration threshold   
2.2 user can modify thread pool param, and these changes can be applied to application's thread pool    
