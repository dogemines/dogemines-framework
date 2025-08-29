package net.dogemines.framework.npc;

public enum PlayerSkin {
    TEST("ewogICJ0aW1lc3RhbXAiIDogMTc1NjE3NzcwMjE5NywKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZTYxNGMwYmFhMTc0NzhkZDRlNzRiOTc2ZWE5MTY4Mzk5NWMyMDk3MDlmNjllOGUwMGEzN2VkYmIzZmRmMGQ2IgogICAgfQogIH0KfQ==",
            "Mu295WelY2TM0Q3ME1qeRrFm1j5svPxVmh5RTFnzKabvBypILhgrOK2+6pIXBjJU/53lVa9Edjqp1K1yEU4VQyd9S8ftDIzsj3ucQwwD0AOna8Ci5tg/izhwq2t5aFO/m8SDY3WeBldX4EElA+jGC1ssPmgUOJqopmxGiGAye9uQPg7WXTk6NwKl4gLgvKWNONZScRjmlkLFNiMWMNMKxeecvbpFNrNqUlNTYH9mY2KZwpyLL9ORem3ye6LCbIIxhfA/EsEsERllJ6m+yJZb7tD3VpsO9Ylj76dMgTGDDApEoP3fbY1ubtwfagQ55XvbxbdhhMEAHPPWt/4205L5j+Hml+IjHPSw0csneanhc7z4lciu/YCotzclm4QCFijDj6jDG0XNUZ4WbXIBEjsVVd4BgjHkniLLDBxhqcU7C4zz9PZelg7L5L9T01QciQrXys70lqbZmb5hMfYc18sSq9UZSabRL+jIlKUHkIoqAG6gYEv3G/CHJjbT3X1zFt35WGpI1HoKbhOwY3rtTOtFwM1QY/Q/NdaBTHZ0rP/H79RPsSJDbzwrhgUlB3X6+yeceg64zM22yZSUX/qqhkKqE8INi0JXcuxgCRmLFnNuSsJnOvgEEqbuJohuK3FJ4awlCk/zJT6BbwMOCEgpzXe/lxEwax3Iml5vDXCjgOmxWpA=");

    private final String value;
    private final String signature;

    PlayerSkin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }
    public String getSignature() {
        return signature;
    }
}