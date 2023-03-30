# Идея со stackoverflow

When thinking about clean architecture You should think of an Android Service in similar way as on any other Android
application component like Activity for example.

Service and Activity can both do similar things like playing music, performing network requests etc.
with one difference being the lack of user-interface in case of a Service
(although one could think of a Notification as an UI for a Service). This lack of UI might be misleading at first.

Having in mind similar purposes of those application components we can imagine that both Service and Activity
can be located in the same layer of the clean architecture project.

First I wouldn't choose the data layer as it is a place for Your data sources
(concrete implementations of the abstractions defined in the domain layer) like web APIs or database controllers.
One would argue that a Service is somewhat of a data source for the application because
You can bind to it and get data from the background but if You look at Activities or Fragments You will notice
that they can also be data sources for other Activities by using the Intents or arguments.

I wouldn't choose the domain package as well - it is supposed to contain definitions of the business logic of the app -
interfaces of repositories etc. Moreover, this layer should be platform-independent,
so no Android components should be there. A Service definitely does not belong there.

The app (or presentation) layer is left. It is an Android-specific layer - and this is the place for a Service.
Service can communicate directly with a neighbour domain layer to access the data via use-cases that
it can pass to a bound Activity, display a Notification with the data or whatever.
All that while not violating the clean architecture rules.