<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<header class="mdc-top-app-bar">
    <nav class="mdc-top-app-bar__row">
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
            <button class="material-icons mdc-top-app-bar__navigation-icon mdc-icon-button"
                    aria-label="Open navigation menu">menu
            </button>
        </section>
        <section class="mdc-top-app-bar__section partage-top-app-bar__section--align-center">
            <span class="mdc-top-app-bar__title">Your boards</span>
        </section>
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-end" role="toolbar">
            <a href="participants.html" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
               aria-label="Participants">
                people_outline
            </a>
            <a href="board-edit.html" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
               aria-label="Add board">add
            </a>
            <div class="mdc-menu-surface--anchor">
                <button id="settings-btn" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                        aria-label="Settings">more_vert
                </button>
                <div class="mdc-menu mdc-menu-surface">
                    <ul class="mdc-list" role="menu" aria-hidden="true" aria-orientation="vertical" tabindex="-1">
                        <li class="mdc-list-item" role="menuitem">
                            <span class="mdc-list-item__ripple"></span>
                            <a href="settings.html" class="mdc-list-item__text">Settings</a>
                        </li>
                        <li role="separator" class="mdc-list-divider"></li>
                        <li class="mdc-list-item" role="menuitem">
                            <span class="mdc-list-item__ripple"></span>
                            <span class="mdc-list-item__text">Logout</span>
                        </li>
                    </ul>
                </div>
            </div>

        </section>
    </nav>
</header>